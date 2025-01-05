package com.d2.productservice.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.core.api.API;
import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.resolver.AdminUserAuthInjection;
import com.d2.productservice.application.port.in.TeacherUseCase;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.Teacher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class TeacherController {

	private final TeacherUseCase teacherUseCase;

	@GetMapping("v1/teachers/by-admin-user/{adminUserId}")
	public API<Teacher> getTeacherByAdminUserId(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long adminUserId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(teacherUseCase.getTeacher(adminUserId));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);

	}

	@GetMapping("v1/teachers/{teacherId}/courses")
	public API<List<Course>> getTeacherCourseList(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long teacherId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(teacherUseCase.getTeacherCourseList(teacherId));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}
}
