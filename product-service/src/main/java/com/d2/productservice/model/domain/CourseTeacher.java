package com.d2.productservice.model.domain;

import com.d2.productservice.model.dto.TeacherDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseTeacher {
	private final Long id;

	private final Long courseId;

	private final String name;

	public static CourseTeacher from(Long courseId, TeacherDto teacherDto) {
		return new CourseTeacher(
			teacherDto.getId(),
			courseId,
			teacherDto.getName()
		);
	}
}
