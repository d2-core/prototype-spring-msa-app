package com.d2.productservice.adapter.out.persistence.teacher;

import org.springframework.stereotype.Component;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.productservice.adapter.out.persistence.course.QCourseJpaEntity;
import com.d2.productservice.application.port.out.TeacherPort;
import com.d2.productservice.model.dto.TeacherDto;
import com.d2.productservice.model.dto.TeacherTeamMemberDto;
import com.d2.productservice.model.enums.TeacherTeamMemberState;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TeacherPersistenceAdapter implements TeacherPort {
	private final TeacherJpaRepository teacherJpaRepository;
	private final TeacherTeamMemberJpaRepository teacherTeamMemberJpaRepository;
	private final JPQLQueryFactory jpqlQueryFactory;

	@Override
	public TeacherDto registerTeacher(String name, String email, String phoneNumber) {
		TeacherJpaEntity teacherJpaEntity = teacherJpaRepository.save(new TeacherJpaEntity(name, email, phoneNumber));
		return TeacherDto.from(teacherJpaEntity);
	}

	@Override
	public TeacherTeamMemberDto registerTeacherTeamMember(Long adminUserId, Long teacherId,
		TeacherTeamMemberState teacherTeamMemberState) {
		TeacherTeamMemberJpaEntity teacherTeamMemberJpaEntity = teacherTeamMemberJpaRepository.save(
			new TeacherTeamMemberJpaEntity(adminUserId, teacherId, teacherTeamMemberState));
		return TeacherTeamMemberDto.from(teacherTeamMemberJpaEntity);
	}

	@Override
	public TeacherDto getTeacher(Long courseId) {
		QTeacherJpaEntity teacherJpaEntity = QTeacherJpaEntity.teacherJpaEntity;
		QCourseJpaEntity courseJpaEntity = QCourseJpaEntity.courseJpaEntity;
		TeacherJpaEntity selectTeacher = jpqlQueryFactory.selectFrom(teacherJpaEntity)
			.join(courseJpaEntity)
			.on(courseJpaEntity.teacherId.eq(teacherJpaEntity.id))
			.fetchFirst();

		if (selectTeacher != null) {
			return TeacherDto.from(selectTeacher);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "courseId: %s".formatted(courseId));
	}

	@Override
	public TeacherDto getTeacherByAdminUserId(Long adminUserId) {
		QTeacherJpaEntity teacherJpaEntity = QTeacherJpaEntity.teacherJpaEntity;
		QTeacherTeamMemberJpaEntity teacherTeamMemberJpaEntity = QTeacherTeamMemberJpaEntity.teacherTeamMemberJpaEntity;
		TeacherJpaEntity resultEntity = jpqlQueryFactory.selectFrom(teacherJpaEntity)
			.join(teacherTeamMemberJpaEntity)
			.on(teacherJpaEntity.id.eq(teacherTeamMemberJpaEntity.teacherJpaEntity.id)
				.and(teacherTeamMemberJpaEntity.adminUserId.eq(adminUserId)))
			.fetchFirst();

		if (resultEntity != null) {
			return TeacherDto.from(resultEntity);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "adminUserId: %s".formatted(adminUserId));
	}

	@Override
	public Long getTeacherIdByAdminUserId(Long adminUserId) {
		QTeacherJpaEntity teacherJpaEntity = QTeacherJpaEntity.teacherJpaEntity;
		QTeacherTeamMemberJpaEntity teacherTeamMemberJpaEntity = QTeacherTeamMemberJpaEntity.teacherTeamMemberJpaEntity;
		Long result = jpqlQueryFactory.select(teacherJpaEntity.id)
			.from(teacherJpaEntity)
			.join(teacherTeamMemberJpaEntity)
			.on(teacherJpaEntity.id.eq(teacherTeamMemberJpaEntity.teacherJpaEntity.id)
				.and(teacherTeamMemberJpaEntity.adminUserId.eq(adminUserId)))
			.fetchFirst();

		if (result != null) {
			return result;
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, "adminUserId: %s".formatted(adminUserId));
	}
}
