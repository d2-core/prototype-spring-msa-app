package com.d2.productservice.model.domain;

import java.time.LocalDateTime;

import com.d2.productservice.model.dto.TeacherDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teacher {
	private final Long id;

	private final String name;

	private final String email;

	private final String phoneNumber;

	private final LocalDateTime createdAt;

	private final LocalDateTime updatedAt;

	public static Teacher from(TeacherDto teacherDto) {
		return new Teacher(
			teacherDto.getId(),
			teacherDto.getName(),
			teacherDto.getEmail(),
			teacherDto.getPhoneNumber(),
			teacherDto.getCreatedAt(),
			teacherDto.getUpdatedAt()
		);
	}
}
