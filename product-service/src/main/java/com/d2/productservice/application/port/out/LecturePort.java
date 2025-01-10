package com.d2.productservice.application.port.out;

import java.util.List;

import com.d2.productservice.model.dto.DeleteFileDto;
import com.d2.productservice.model.dto.LectureDto;
import com.d2.productservice.model.dto.LectureReferenceDto;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.dto.VideoStreamDto;
import com.d2.productservice.model.enums.LectureExportType;
import com.d2.productservice.model.enums.LectureType;

public interface LecturePort {
	LectureDto register(Long courseId, LectureType lectureType, String title, String thumbnailImageUrl,
		String description, List<LectureTimelineDto> chapters, List<LectureReferenceDto> lectureReferences,
		LectureExportType lectureExportType);

	LectureDto update(Long lectureId, Long courseId, LectureType lectureType, String title, String thumbnailImageUrl,
		String description, List<LectureTimelineDto> chapters, List<LectureReferenceDto> lectureReferences,
		LectureExportType lectureExportType);

	LectureDto update(Long lectureId, String thumbnailImageUrl, List<LectureReferenceDto> lectureReferences);

	LectureDto update(Long lectureId, LectureExportType lectureExportType);

	LectureDto update(Long lectureId, Long videoStreamId);

	void delete(Long lectureId);

	List<LectureDto> getLectureList();

	List<LectureDto> getLectureList(Long courseId);

	List<VideoStreamDto> getLectureVideoConditionList(List<Long> lectureIds);

	LectureDto getLecture(Long lectureId);

	List<DeleteFileDto> getFileUrls(Long lectureId);
}
