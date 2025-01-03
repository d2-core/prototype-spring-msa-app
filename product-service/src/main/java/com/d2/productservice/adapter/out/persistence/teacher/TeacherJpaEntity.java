package com.d2.productservice.adapter.out.persistence.teacher;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "teachers")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TeacherJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String phoneNumber;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "teacherJpaEntity", fetch = FetchType.LAZY)
	private List<TeacherTeamMemberJpaEntity> teacherTeamMemberJpaEntities = List.of();

	void addTeacherTeamMemberJpaEntity(TeacherTeamMemberJpaEntity teacherTeamMemberJpaEntity) {
		teacherTeamMemberJpaEntities.add(teacherTeamMemberJpaEntity);
	}

	public TeacherJpaEntity(String name, String email, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public TeacherJpaEntity(Long id) {
		this.id = id;
	}
}
