package com.d2.productservice.adapter.out.persistence.video;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.dto.VideoConvertDto;
import com.d2.core.model.enums.VideoResolution;
import com.d2.productservice.application.port.out.VideoStreamPort;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.VideoTranscodeStatus;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VideoStreamPersistenceAdapter implements VideoStreamPort {

	private final VideoStreamJpaRepository videoStreamJpaRepository;
	private final JPQLQueryFactory jpqlQueryFactory;

	@Value("${ffprobe.path}")
	private String ffprobePath;

	@Override
	public VideoStreamDto register(VideoConvertDto videoConvertDto) {
		try {
			MultipartFile videoFile = videoConvertDto.getFile();

			Long originalFileSize = videoFile.getSize();

			FFprobe ffprobe = new FFprobe(ffprobePath);
			FFmpegProbeResult probeResult = ffprobe.probe(
				videoConvertDto.getMultipartTempInputFile().getAbsolutePath());

			FFmpegFormat format = probeResult.getFormat();
			String videoFormatName = format.format_name;
			int duration = (int)format.duration;

			FFmpegStream videoStream = probeResult.getStreams().stream()
				.filter(stream -> stream.codec_type == FFmpegStream.CodecType.VIDEO)
				.findFirst()
				.orElseThrow(() -> new ApiExceptionImpl(ErrorCodeImpl.BAD_REQUEST, "No video stream found"));

			Integer originalHeight = videoStream.height;  // 원본 비디오의 높이

			List<String> originalSupportedQualities = probeResult.getStreams().stream()
				.filter(stream -> stream.codec_type == FFmpegStream.CodecType.VIDEO)
				.map(stream -> stream.height + "p")
				.collect(Collectors.toList());

			validateResolutions(videoConvertDto.getTargetVideoResolutions(), originalHeight);

			VideoSteamJpaEntity videoSteamJpaEntity = videoStreamJpaRepository.save(
				new VideoSteamJpaEntity(
					videoConvertDto.getVideoUniqueKey(),
					duration,
					videoFormatName,
					originalFileSize,
					videoConvertDto.getTargetVideoResolutions(),
					originalSupportedQualities,
					VideoTranscodeStatus.CREATE
				)
			);

			return VideoStreamDto.from(videoSteamJpaEntity);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}
	}

	@Override
	public VideoStreamDto update(Long videoStreamId, String videoUrl, Integer progress,
		VideoTranscodeStatus videoTranscodeStatus) {
		VideoSteamJpaEntity videoSteamJpaEntity = getVideoSteamJpaEntity(videoStreamId);

		videoStreamJpaRepository.save(videoSteamJpaEntity.update(videoUrl, progress, videoTranscodeStatus));

		return VideoStreamDto.from(videoSteamJpaEntity);
	}

	@Override
	public VideoStreamDto update(Long videoStreamId, Integer progress, VideoTranscodeStatus videoTranscodeStatus) {
		VideoSteamJpaEntity videoSteamJpaEntity = getVideoSteamJpaEntity(videoStreamId);

		videoStreamJpaRepository.save(videoSteamJpaEntity.update(progress, videoTranscodeStatus));

		return VideoStreamDto.from(videoSteamJpaEntity);
	}

	@Override
	public VideoStreamDto update(Long videoStreamId, VideoTranscodeStatus videoTranscodeStatus) {

		VideoSteamJpaEntity videoSteamJpaEntity = getVideoSteamJpaEntity(videoStreamId);

		videoStreamJpaRepository.save(videoSteamJpaEntity.update(videoTranscodeStatus));

		return VideoStreamDto.from(videoSteamJpaEntity);
	}

	@Override
	public VideoStreamDto getVideoStream(Long videoStreamId) {
		VideoSteamJpaEntity videoSteamJpaEntity = getVideoSteamJpaEntity(videoStreamId);

		return VideoStreamDto.from(videoSteamJpaEntity);
	}

	@Override
	public void delete(Long videoStreamId) {
		videoStreamJpaRepository.deleteById(videoStreamId);
	}

	@Override
	public void deleteByVideoUniqueKey(String videoUniqueKey) {
		QVideoSteamJpaEntity videoSteamJpaEntity = QVideoSteamJpaEntity.videoSteamJpaEntity;
		jpqlQueryFactory.delete(videoSteamJpaEntity)
			.where(videoSteamJpaEntity.videoUniqueKey.eq(videoUniqueKey));

	}

	private VideoSteamJpaEntity getVideoSteamJpaEntity(Long videoStreamId) {
		return videoStreamJpaRepository.findById(videoStreamId)
			.orElseThrow(() -> new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND));
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
}
