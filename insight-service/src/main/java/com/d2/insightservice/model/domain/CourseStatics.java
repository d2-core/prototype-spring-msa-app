package com.d2.insightservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseStatics {
	private final Long id;
	private final Long courseId;
	private final Double rating;
	private final Integer viewCount;
	private final LectureStatics lectureStatistics;
	private final StudentStatics studentStatics;

	@Data
	@AllArgsConstructor
	public static class LectureStatics {
		private final Integer totalLectures;
		private final Integer totalDuration;
		private final Integer averageDuration;
	}

	@Data
	@AllArgsConstructor
	public static class StudentStatics {
		private final Integer totalStudent;
		private final Integer activeStudent;
		private final Integer completeStudent;
	}
}
