package com.d2.productservice.adapter.out.persistence.teacher;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.productservice.model.enums.TeacherTeamMemberState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Table(name = "teacher_team_members")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TeacherTeamMemberJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long adminUserId;

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private TeacherTeamMemberState teacherTeamMemberState;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private TeacherJpaEntity teacherJpaEntity;

	public TeacherTeamMemberJpaEntity(Long adminUserId, Long teacherId, TeacherTeamMemberState teacherTeamMemberState) {
		TeacherJpaEntity teacherJpaEntity = new TeacherJpaEntity(teacherId);
		this.adminUserId = adminUserId;
		this.teacherTeamMemberState = teacherTeamMemberState;
		this.teacherJpaEntity = teacherJpaEntity;
	}
}
