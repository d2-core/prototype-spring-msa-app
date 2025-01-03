package com.d2.productservice.model.dto;

import java.time.LocalDateTime;

import com.d2.productservice.adapter.out.persistence.teacher.TeacherJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
	private Long id;

	private String name;

	private String email;

	private String phoneNumber;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public static TeacherDto from(TeacherJpaEntity teacherJpaEntity) {
		return new TeacherDto(
			teacherJpaEntity.getId(),
			teacherJpaEntity.getName(),
			teacherJpaEntity.getEmail(),
			teacherJpaEntity.getPhoneNumber(),
			teacherJpaEntity.getCreatedAt(),
			teacherJpaEntity.getUpdatedAt()
		);
	}
}
