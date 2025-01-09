package com.d2.productservice.application.port.in;

import java.io.File;
import java.util.List;

import com.d2.core.model.dto.FileFormDto;
import com.d2.core.model.dto.ReferenceFileFormDto;
import com.d2.productservice.model.domain.Lecture;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.d2.productservice.model.enums.LectureExportType;

public interface LectureUseCase {
	Lecture registerVideoLecture(Long courseId, String title, FileFormDto thumbnailFile,
		File mutipartTempInputFile, FileFormDto videoFile, String description,
		List<LectureTimelineDto> chapters, List<ReferenceFileFormDto> lectureReferenceFiles,
		LectureExportType lectureExportType);

	Lecture registerDocumentLecture(Long courseId, String title, FileFormDto thumbnailFile,
		String description, List<ReferenceFileFormDto> lectureReferenceFiles,
		LectureExportType lectureExportType);

	Lecture updateVideoLecture(Long lectureId, Long courseId, String title,
		FileFormDto thumbnailFile, File mutipartTempInputFile, FileFormDto videoFile,
		String description, List<LectureTimelineDto> chapters,
		List<ReferenceFileFormDto> lectureReferenceFiles, LectureExportType lectureExportType);

	Lecture updateDocumentLecture(Long lectureId, Long courseId, String title,
		FileFormDto thumbnailFile, String description,
		List<ReferenceFileFormDto> lectureReferenceFiles, LectureExportType lectureExportType);

	void deleteLecture(Long lectureId);

	List<Lecture> getLectureList();

	Lecture getLecture(Long lectureId);

	void updateExportType(Long lectureId, LectureExportType lectureExportType);
}
