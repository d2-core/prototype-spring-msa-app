package com.d2.productservice.application.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.application.port.out.HlsVideoStoragePort;
import com.d2.core.application.port.out.ObjectStoragePort;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.dto.FileFormDto;
import com.d2.core.model.dto.ReferenceFileFormDto;
import com.d2.core.model.dto.VideoConvertDto;
import com.d2.core.model.enums.ReferenceType;
import com.d2.core.model.enums.VideoResolution;
import com.d2.core.utils.FileManager;
import com.d2.productservice.application.port.in.LectureUseCase;
import com.d2.productservice.application.port.out.LecturePort;
import com.d2.productservice.application.port.out.SendPort;
import com.d2.productservice.application.port.out.VideoStreamPort;
import com.d2.productservice.model.SendLectureEvent;
import com.d2.productservice.model.domain.Lecture;
import com.d2.productservice.model.domain.LectureVideoCondition;
import com.d2.productservice.model.dto.DeleteFileDto;
import com.d2.productservice.model.dto.LectureDto;
import com.d2.productservice.model.dto.LectureReferenceDto;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.FileType;
import com.d2.productservice.model.enums.LectureEvent;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureType;
import com.d2.productservice.model.enums.VideoTranscodeStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LectureService implements LectureUseCase {
	private record FilesDto(String thumbnailUrl, List<LectureReferenceDto> lectureReferences) {
	}

	private final LecturePort lecturePort;
	private final SendPort sendPort;
	private final ObjectStoragePort objectStoragePort;
	private final HlsVideoStoragePort hlsVideoStoragePort;
	private final VideoStreamPort videoStreamPort;
	private final FileManager fileManager;

	@Transactional
	@Override
	public Lecture registerVideoLecture(Long courseId, String title, FileFormDto thumbnailFile,
		File mutipartTempInputFile, FileFormDto videoFile, String description,
		List<LectureTimelineDto> chapters, List<ReferenceFileFormDto> lectureReferenceFiles,
		LectureExportType lectureExportType) {

		List<String> rollbackFileUrls = new ArrayList<>();
		return executeWithRollback(() -> {
			LectureDto lectureDto = lecturePort.register(courseId, LectureType.VIDEO, title, "", description,
				chapters, new ArrayList<>(), lectureExportType);

			FilesDto filesDto = processFiles(thumbnailFile, lectureReferenceFiles, rollbackFileUrls);

			lectureDto = lecturePort.update(lectureDto.getId(), filesDto.thumbnailUrl(), filesDto.lectureReferences());

			if (videoFile != null) {
				registerLectureVideo(lectureDto.getId(), mutipartTempInputFile, videoFile);
			}

			sendPort.sendLectureEvent(new SendLectureEvent(lectureDto.getId(), LectureEvent.UPSERT));
			return Lecture.from(lectureDto);
		}, rollbackFileUrls);
	}

	@Transactional
	@Override
	public Lecture registerDocumentLecture(Long courseId, String title, FileFormDto thumbnailFile,
		String description, List<ReferenceFileFormDto> lectureReferenceFiles,
		LectureExportType lectureExportType) {

		List<String> rollbackFileUrls = new ArrayList<>();
		return executeWithRollback(() -> {
			LectureDto lectureDto = lecturePort.register(courseId, LectureType.DOCUMENT, title, "", description,
				new ArrayList<>(), new ArrayList<>(), lectureExportType);

			FilesDto filesDto = processFiles(thumbnailFile, lectureReferenceFiles, rollbackFileUrls);

			lectureDto = lecturePort.update(lectureDto.getId(), filesDto.thumbnailUrl(), filesDto.lectureReferences());

			sendPort.sendLectureEvent(new SendLectureEvent(lectureDto.getId(), LectureEvent.UPSERT));
			return Lecture.from(lectureDto);
		}, rollbackFileUrls);
	}

	@Transactional
	@Override
	public Lecture updateVideoLecture(Long lectureId, Long courseId, String title,
		FileFormDto thumbnailFile, File mutipartTempInputFile, FileFormDto videoFile,
		String description, List<LectureTimelineDto> chapters,
		List<ReferenceFileFormDto> lectureReferenceFiles, LectureExportType lectureExportType) {

		List<String> rollbackFileUrls = new ArrayList<>();
		return executeWithRollback(() -> {
			FilesDto filesDto = processFiles(thumbnailFile, lectureReferenceFiles, rollbackFileUrls);

			LectureDto lectureDto = lecturePort.update(lectureId, courseId, LectureType.VIDEO, title,
				filesDto.thumbnailUrl(), description, chapters, filesDto.lectureReferences(),
				lectureExportType);

			if (videoFile != null && videoFile.getFile() != null) {
				updateLectureVideo(lectureDto.getId(), lectureDto.getVideoStreamDto().getId(),
					mutipartTempInputFile, videoFile);
			}

			sendPort.sendLectureEvent(new SendLectureEvent(lectureDto.getId(), LectureEvent.UPSERT));
			return Lecture.from(lectureDto);
		}, rollbackFileUrls);
	}

	@Transactional
	@Override
	public Lecture updateDocumentLecture(Long lectureId, Long courseId, String title,
		FileFormDto thumbnailFile, String description,
		List<ReferenceFileFormDto> lectureReferenceFiles, LectureExportType lectureExportType) {

		List<String> rollbackFileUrls = new ArrayList<>();
		return executeWithRollback(() -> {
			FilesDto filesDto = processFiles(thumbnailFile, lectureReferenceFiles, rollbackFileUrls);

			LectureDto lectureDto = lecturePort.update(lectureId, courseId, LectureType.DOCUMENT, title,
				filesDto.thumbnailUrl(), description, new ArrayList<>(), filesDto.lectureReferences(),
				lectureExportType);

			sendPort.sendLectureEvent(new SendLectureEvent(lectureDto.getId(), LectureEvent.UPSERT));
			return Lecture.from(lectureDto);
		}, rollbackFileUrls);
	}

	@Transactional
	@Override
	public void deleteLecture(Long lectureId) {
		List<DeleteFileDto> fileUrls = lecturePort.getFileUrls(lectureId);

		lecturePort.delete(lectureId);

		fileUrls.forEach((deleteFileDto) -> {
			if (deleteFileDto.getFileType().equals(FileType.VIDEO)) {
				hlsVideoStoragePort.deleteVideo(deleteFileDto.getUrl());
			}
			if (deleteFileDto.getFileType().equals(FileType.IMAGE) || deleteFileDto.getFileType()
				.equals(FileType.PDF)) {
				objectStoragePort.deleteFile(deleteFileDto.getUrl());
			}
		});

		sendPort.sendLectureEvent(new SendLectureEvent(lectureId, LectureEvent.DELETE));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Lecture> getLectureList() {
		return lecturePort.getLectureList()
			.stream().map(Lecture::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Lecture getLecture(Long lectureId) {
		LectureDto lectureDto = lecturePort.getLecture(lectureId);

		return Lecture.from(lectureDto);
	}

	@Transactional(readOnly = true)
	@Override
	public List<LectureVideoCondition> getLectureVideoConditionList(List<Long> lectureIds) {
		return lecturePort.getLectureVideoConditionList(lectureIds)
			.stream().map(LectureVideoCondition::from)
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void updateExportType(Long lectureId, LectureExportType lectureExportType) {
		lecturePort.update(lectureId, lectureExportType);
		sendPort.sendLectureEvent(new SendLectureEvent(lectureId, LectureEvent.UPDATE_EXPORT_TYPE));
	}

	private Lecture executeWithRollback(Callable<Lecture> logic, List<String> rollbackFileUrls) {
		try {
			return logic.call();
		} catch (Exception ex) {
			objectStoragePort.deleteFiles(rollbackFileUrls);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}
	}

	private FilesDto processFiles(FileFormDto thumbnailFile,
		List<ReferenceFileFormDto> lectureReferenceFiles, List<String> rollbackFileUrls) {

		List<LectureReferenceDto> lectureReferences = getLectureReferenceList(lectureReferenceFiles);
		String thumbnailUrl = getThumbNailImageUrl(thumbnailFile);

		rollbackFileUrls.add(thumbnailUrl);
		rollbackFileUrls.addAll(lectureReferences.stream().map(LectureReferenceDto::getUrl).toList());

		return new FilesDto(thumbnailUrl, lectureReferences);
	}

	public void registerLectureVideo(Long lectureId, File mutipartTempInputFile, FileFormDto videoFile) {
		try {
			String videoUniqueKey = hlsVideoStoragePort.getVideoId();
			List<VideoResolution> supportedVideoResolutions = List.of(VideoResolution.P360);
			VideoConvertDto videoConvertDto = new VideoConvertDto(videoUniqueKey, videoFile.getFile(),
				mutipartTempInputFile, supportedVideoResolutions);

			VideoStreamDto videoStreamDto = videoStreamPort.register(videoConvertDto);

			lecturePort.update(lectureId, videoStreamDto.getId());

			uploadVideo(videoUniqueKey, videoStreamDto.getId(), videoConvertDto);
		} catch (Exception ex) {
			fileManager.cleanup(mutipartTempInputFile);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}
	}

	private void updateLectureVideo(Long lectureId, Long videoStreamId, File mutipartTempInputFile,
		FileFormDto videoFile) {

		try {
			String videoUniqueKey = hlsVideoStoragePort.getVideoId();
			List<VideoResolution> supportedVideoResolutions = List.of(VideoResolution.P360);
			VideoConvertDto videoConvertDto = new VideoConvertDto(videoUniqueKey, videoFile.getFile(),
				mutipartTempInputFile, supportedVideoResolutions);

			VideoStreamDto videoStreamDto = videoStreamPort.register(videoConvertDto);

			lecturePort.update(lectureId, videoStreamDto.getId());

			videoStreamPort.delete(videoStreamId);

			uploadVideo(videoUniqueKey, videoStreamDto.getId(), videoConvertDto);
		} catch (Exception ex) {
			fileManager.cleanup(mutipartTempInputFile);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}
	}

	private void uploadVideo(String videoUniqueKey, Long videoStreamId, VideoConvertDto videoConvertDto) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setThreadNamePrefix("video-stream");
		executor.initialize();
		CompletableFuture.runAsync(() -> {
			String expectedVideoUrl = hlsVideoStoragePort.getExpectedVideoUrl(videoUniqueKey);
			try {
				String uploadVideoUrl = hlsVideoStoragePort.uploadVideo(videoUniqueKey, videoConvertDto,
					(progress) -> videoStreamPort.update(videoStreamId, progress, VideoTranscodeStatus.PROGRESS));
				videoStreamPort.update(videoStreamId, uploadVideoUrl, 100, VideoTranscodeStatus.COMPLETE);
			} catch (Exception ex) {
				log.error("video-stream-async error", ex);
				hlsVideoStoragePort.deleteVideo(expectedVideoUrl);
				videoStreamPort.update(videoStreamId, VideoTranscodeStatus.FAIL);
				throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
			} finally {
				fileManager.cleanup(videoConvertDto.getMultipartTempInputFile());
			}
		}, executor);
	}

	private String getThumbNailImageUrl(FileFormDto thumbnailFile) {
		String thumbnailUrl = "";
		if (thumbnailFile != null) {
			thumbnailUrl = thumbnailFile.getUrl();

			if (thumbnailFile.getFile() != null) {
				thumbnailUrl = objectStoragePort.uploadFile(thumbnailFile.getFile());
			}
		}
		return thumbnailUrl;
	}

	private List<LectureReferenceDto> getLectureReferenceList(List<ReferenceFileFormDto> lectureReferenceFiles) {
		return lectureReferenceFiles.stream()
			.map(referenceFileFormDto -> {
				if (referenceFileFormDto.getType().equals(ReferenceType.PDF)) {
					String fileUrl = objectStoragePort.uploadFile(referenceFileFormDto.getFile());
					return new LectureReferenceDto(referenceFileFormDto.getType(),
						referenceFileFormDto.getDescription(), fileUrl);
				} else if (referenceFileFormDto.getType().equals(ReferenceType.LINK)) {
					return new LectureReferenceDto(referenceFileFormDto.getType(),
						referenceFileFormDto.getDescription(), referenceFileFormDto.getUrl());
				}
				return new LectureReferenceDto();
			})
			.collect(Collectors.toList());
	}
}
