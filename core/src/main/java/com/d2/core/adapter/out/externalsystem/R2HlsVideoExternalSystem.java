package com.d2.core.adapter.out.externalsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import com.amazonaws.event.ProgressEventType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.d2.core.application.port.out.HlsVideoStoragePort;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.dto.VideoConvertDto;
import com.d2.core.model.enums.VideoResolution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class R2HlsVideoExternalSystem implements HlsVideoStoragePort {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucketName:null}")
	private String bucketName;

	@Value("${url.r2:null}")
	private String r2Url;

	@Value("${aws.s3.urlPrefix:null}")
	private String urlPrefix;

	@Value("${ffmpeg.path:null}")
	private String ffmpegPath;

	@Value("${ffprobe.path:null}")
	private String ffprobePath;

	@Override
	public String getExpectedVideoUrl(String videoId) {
		return r2Url + "/" + urlPrefix + "/videos/" + videoId + "/playlist.m3u8";
	}

	@Override
	public String uploadVideo(String videoId, VideoConvertDto videoConvertDto, Consumer<Integer> progressConsumer) {
		validateSettings();
		String videoUrl = getExpectedVideoUrl(videoId);
		if (videoConvertDto == null || videoConvertDto.getFile() == null) {
			return "";
		}
		long maxFileSizeBites = 500L * 1024 * 1024;
		if (videoConvertDto.getFile().getSize() >= maxFileSizeBites) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY, "지원하는 파일 사이즈 초과");
		}

		File hlsTempOutputDir = null;
		try {
			hlsTempOutputDir = Files.createTempDirectory("hls").toFile();
			convertToHls(videoConvertDto.getMultipartTempInputFile().getAbsolutePath(),
				hlsTempOutputDir.getAbsolutePath(), videoConvertDto);

			long totalCount = calculateDirectorySize(hlsTempOutputDir);
			uploadHlsFiles(hlsTempOutputDir.getAbsoluteFile(), videoId, totalBytesTransferred -> {
				int progressPercentage = Math.min(100,
					(int)((totalBytesTransferred * 100L) / totalCount));
				if (progressConsumer != null) {
					progressConsumer.accept(progressPercentage);
				}
			});

			return videoUrl;
		} catch (Exception ex) {
			log.error("error hls", ex);
			deleteVideo(videoUrl);
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY, ex);
		} finally {
			if (videoConvertDto.getMultipartTempInputFile() != null && hlsTempOutputDir != null) {
				cleanup(videoConvertDto.getMultipartTempInputFile(), hlsTempOutputDir);
			}
		}
	}

	@Override
	public String deleteVideo(String videoUrl) {
		validateSettings();

		if (videoUrl == null || videoUrl.isEmpty()) {
			return "";
		}

		try {
			String key = extractKeyFromUrl(videoUrl);
			String videoPrefix = key.substring(0, key.lastIndexOf("/") + 1);

			ListObjectsV2Request listRequest = new ListObjectsV2Request()
				.withBucketName(bucketName)
				.withPrefix(videoPrefix);

			ListObjectsV2Result objects = amazonS3.listObjectsV2(listRequest);

			for (S3ObjectSummary object : objects.getObjectSummaries()) {
				amazonS3.deleteObject(bucketName, object.getKey());
			}

			return videoUrl;
		} catch (Exception ex) {
			log.error("error hls", ex);
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY, ex);
		}
	}

	private void validateSettings() {
		if (bucketName == null || r2Url == null || ffmpegPath == null || ffprobePath == null) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "Please check yml settings");
		}
	}

	private void cleanup(File tempInputFile, File tempOutputDir) {
		try {
			tempInputFile.delete();
			FileUtils.deleteDirectory(tempOutputDir);
		} catch (IOException ex) {
			log.error("error hls", ex);
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY, ex);
		}
	}

	private void convertToHls(String inputPath, String outputDir,
		VideoConvertDto videoConvertDto) throws IOException {
		FFmpeg ffmpeg = new FFmpeg(ffmpegPath);

		File inputFile = new File(inputPath);
		if (!inputFile.exists()) {
			throw new IOException("Input file not found: " + inputPath);
		}

		if (!inputFile.canRead()) {
			throw new IOException("Cannot read input file: " + inputPath);
		}
		FFprobe ffprobe = new FFprobe(ffprobePath);

		FFmpegProbeResult probeResult = ffprobe.probe(inputPath);
		FFmpegStream videoStream = probeResult.getStreams().stream()
			.filter(stream -> stream.codec_type == FFmpegStream.CodecType.VIDEO)
			.findFirst()
			.orElseThrow(() -> new ApiExceptionImpl(ErrorCodeImpl.BAD_REQUEST, "잘못된 비디오 포맷"));

		Integer originalHeight = videoStream.height;
		validateResolutions(videoConvertDto.getTargetVideoResolutions(), originalHeight);

		List<FFmpegBuilder> builders = new ArrayList<>();

		for (VideoResolution resolution : videoConvertDto.getTargetVideoResolutions()) {
			builders.add(createBuilder(inputPath, outputDir, resolution));
		}

		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		for (FFmpegBuilder builder : builders) {
			executor.createJob(builder).run();
		}

		createMasterPlaylist(outputDir, videoConvertDto.getTargetVideoResolutions());
	}

	private void validateResolutions(List<VideoResolution> targetResolutions, Integer originalHeight) {
		List<VideoResolution> availableResolutions = VideoResolution.getAvailableResolution(originalHeight);

		if (!new HashSet<>(availableResolutions).containsAll(targetResolutions)) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAD_REQUEST,
				"Requested resolution is higher than original video resolution");
		}

		if (!targetResolutions.contains(VideoResolution.P360)) {
			targetResolutions.add(VideoResolution.P360);
		}
	}

	private FFmpegBuilder createBuilder(String inputPath, String outputDir,
		VideoResolution resolution) throws IOException {

		File baseOutputDir = new File(outputDir);
		if (!baseOutputDir.exists()) {
			if (!baseOutputDir.mkdirs()) {
				throw new IOException("출력 디렉토리 생성 실패: " + outputDir);
			}
		}

		File resolutionDir = new File(outputDir, "p" + resolution.getHeight());
		if (!resolutionDir.exists()) {
			if (!resolutionDir.mkdirs()) {
				throw new IOException("해상도 디렉토리 생성 실패: " + resolutionDir.getPath());
			}
		}

		return new FFmpegBuilder()
			.setInput(inputPath)
			.overrideOutputFiles(true)
			.addOutput(outputDir + "/" + resolution.name().toLowerCase() + "/playlist.m3u8")
			.setVideoCodec("libx264")
			.addExtraArgs("-preset", "slow")
			.addExtraArgs("-crf", "18")
			.setVideoResolution(resolution.getWidth(), resolution.getTargetHeight())
			.setVideoBitRate(resolution.getVideoBitrate())
			.addExtraArgs("-c:a", "copy")
			.setFormat("hls")
			.addExtraArgs("-hls_time", "10")
			.addExtraArgs("-hls_list_size", "0")
			.addExtraArgs("-hls_segment_filename",
				outputDir + "/" + resolution.name().toLowerCase() + "/segment_%d.ts")
			.done();
	}

	private void createMasterPlaylist(String outputDir, List<VideoResolution> resolutions)
		throws IOException {
		StringBuilder masterPlaylist = new StringBuilder("#EXTM3U\n#EXT-X-VERSION:3\n\n");

		for (VideoResolution resolution : resolutions) {
			masterPlaylist.append(String.format("#EXT-X-STREAM-INF:BANDWIDTH=%d,RESOLUTION=%dx%d\n",
				resolution.getVideoBitrate(), resolution.getWidth(), resolution.getHeight()));
			masterPlaylist.append(resolution.name().toLowerCase()).append("/playlist.m3u8\n");
		}

		File masterPlaylistFile = new File(outputDir + "/playlist.m3u8");
		FileUtils.writeStringToFile(masterPlaylistFile, masterPlaylist.toString(), "UTF-8");
	}

	private void uploadHlsFiles(File hlsDir, String videoId, Consumer<Long> totalBytesTransferredConsumer) {
		uploadDirectory(hlsDir, videoId, "", totalBytesTransferredConsumer);
	}

	private void uploadDirectory(File directory, String videoId, String subPath,
		Consumer<Long> totalBytesTransferredConsumer) {
		uploadDirectoryInternal(directory, videoId, subPath, totalBytesTransferredConsumer, new AtomicLong(0));
	}

	private void uploadDirectoryInternal(File directory, String videoId, String subPath,
		Consumer<Long> totalBytesTransferredConsumer, AtomicLong totalBytesTransferred) {
		File[] files = directory.listFiles();

		Consumer<Long> bytesTransferredConsumer = bytesTransferred -> {
			long currentTotal = totalBytesTransferred.addAndGet(bytesTransferred);
			if (totalBytesTransferredConsumer != null) {
				totalBytesTransferredConsumer.accept(currentTotal);
			}
		};

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					uploadDirectoryInternal(file, videoId, subPath + file.getName() + "/",
						totalBytesTransferredConsumer, totalBytesTransferred);
				} else {
					uploadFile(file, videoId, subPath, bytesTransferredConsumer);
				}
			}
		}
	}

	private void uploadFile(File file, String videoId, String subPath, Consumer<Long> bytesTransferredConsumer) {
		String key = String.format("%s/videos/%s/%s%s",
			urlPrefix, videoId, subPath, file.getName());
		String contentType = getContentType(file.getName());

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.length());
		metadata.setContentType(contentType);

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileInputStream, metadata)
				.withGeneralProgressListener(progressEvent -> {
					if (progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
						if (bytesTransferredConsumer != null) {
							bytesTransferredConsumer.accept(file.length());
						}
					}
				});

			amazonS3.putObject(putObjectRequest);
		} catch (IOException e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.BAG_GATEWAY,
				"Error uploading HLS file: " + e.getMessage());
		}
	}

	// 1. 총 파일 크기 계산
	private long calculateDirectorySize(File directory) {
		long size = 0;
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					size += calculateDirectorySize(file);
				} else {
					size += file.length();
				}
			}
		}
		return size;
	}

	private String getContentType(String fileName) {
		if (fileName.endsWith(".m3u8")) {
			return "application/x-mpegURL";
		} else if (fileName.endsWith(".ts")) {
			return "video/MP2T";
		}
		return "application/octet-stream";
	}

	private String extractKeyFromUrl(String fileUrl) {
		try {
			int startIndex = fileUrl.indexOf(urlPrefix);
			if (startIndex != -1) {
				return fileUrl.substring(startIndex);
			}
			throw new IllegalArgumentException("URL does not contain the specified prefix");
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not extract key from URL: " + fileUrl);
		}
	}
}
