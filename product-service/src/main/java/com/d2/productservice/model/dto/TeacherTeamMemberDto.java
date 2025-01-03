package com.d2.productservice.model.dto;

import java.time.LocalDateTime;

import com.d2.productservice.adapter.out.persistence.teacher.TeacherTeamMemberJpaEntity;
import com.d2.productservice.model.enums.TeacherTeamMemberState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTeamMemberDto {
	private Long id;

	private Long teacherId;

	private Long adminUserId;

	private TeacherTeamMemberState teacherTeamMemberState;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public static TeacherTeamMemberDto from(TeacherTeamMemberJpaEntity entity) {
		return new TeacherTeamMemberDto(
			entity.getId(),
			entity.getTeacherJpaEntity().getId(),
			entity.getAdminUserId(),
			entity.getTeacherTeamMemberState(),
			entity.getCreatedAt(),
			entity.getUpdatedAt()
		);
	}

}
