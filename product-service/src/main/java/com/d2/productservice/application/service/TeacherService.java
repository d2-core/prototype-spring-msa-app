package com.d2.productservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.core.model.enums.AdminUserRole;
import com.d2.productservice.application.port.in.TeacherUseCase;
import com.d2.productservice.application.port.out.AdminUserPort;
import com.d2.productservice.application.port.out.CoursePort;
import com.d2.productservice.application.port.out.TeacherPort;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.Teacher;
import com.d2.productservice.model.dto.AdminUserDto;
import com.d2.productservice.model.dto.TeacherDto;
import com.d2.productservice.model.enums.TeacherTeamMemberState;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherService implements TeacherUseCase {
	private final TeacherPort teacherPort;
	private final AdminUserPort adminUserPort;
	private final CoursePort coursePort;

	@Transactional
	@Override
	public Teacher registerTeacher(Long adminUserId, AdminUserRole adminUserRole) {
		if (adminUserRole.equals(AdminUserRole.TEACHER)) {
			AdminUserDto adminUserDto = adminUserPort.getAdminUser(adminUserId);
			TeacherDto teacherDto = teacherPort.registerTeacher(adminUserDto.getNickname(), adminUserDto.getEmail(),
				adminUserDto.getPhoneNumber());
			teacherPort.registerTeacherTeamMember(adminUserId, teacherDto.getId(), TeacherTeamMemberState.OWNER);
			return Teacher.from(teacherDto);
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public Teacher getTeacher(Long adminUserId) {
		return Teacher.from(teacherPort.getTeacherByAdminUserId(adminUserId));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Course> getTeacherCourseList(Long teacherId) {
		return coursePort.getTeacherCourseList(teacherId)
			.stream()
			.map(Course::from)
			.collect(Collectors.toList());
	}
}
