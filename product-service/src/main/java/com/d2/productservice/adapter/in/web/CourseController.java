package com.d2.productservice.adapter.in.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.core.api.API;
import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.domain.UserAuth;
import com.d2.core.resolver.AdminUserAuthInjection;
import com.d2.core.resolver.UserAuthInjection;
import com.d2.productservice.application.port.in.CourseUseCase;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.CourseTeacher;
import com.d2.productservice.model.enums.CoursePublishState;
import com.d2.productservice.model.request.CoursePublishRequest;
import com.d2.productservice.model.request.CourseUpsertRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class CourseController {

	private final CourseUseCase courseUseCase;

	@PostMapping(value = "v1/courses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public API<Course> registerCourse(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@Valid @ModelAttribute CourseUpsertRequest request) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			Course course = courseUseCase.upsertCourse(
				null,
				request.getTeacherId(),
				request.getThumbnailImageFiles(),
				request.getCourseCategoryId(), request.getTitle(), request.getSubTitle(),
				request.getDescriptionWithMarkdown(), request.getCourseLevelId(), request.getTags(),
				request.getPrice()
			);
			return API.OK(course);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PutMapping("v1/courses/{courseId}")
	public API<Course> updateCourse(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseId,
		@Valid @ModelAttribute CourseUpsertRequest request) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			Course course = courseUseCase.upsertCourse(
				courseId,
				request.getTeacherId(),
				request.getThumbnailImageFiles(),
				request.getCourseCategoryId(), request.getTitle(), request.getSubTitle(),
				request.getDescriptionWithMarkdown(), request.getCourseLevelId(), request.getTags(),
				request.getPrice()
			);
			return API.OK(course);
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@DeleteMapping("v1/courses/{courseId}")
	public API<Object> deleteCourse(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			courseUseCase.deleteCourse(courseId);
			return API.NO_CONTENT();
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PostMapping("v1/courses/publish")
	public API<Object> publishCourse(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody CoursePublishRequest request
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {

			if (request.getPublishState().equals(CoursePublishState.PUBLISH)) {
				courseUseCase.publishCourse(request.getCourseId());
			} else if (request.getPublishState().equals(CoursePublishState.UN_PUBLISH)) {
				courseUseCase.unPublishCourse(request.getCourseId());
			}
			return API.NO_CONTENT();
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/courses")
	public API<List<Course>> getCourseList(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@UserAuthInjection UserAuth userAuth
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			List<Course> course = courseUseCase.getCourseList();
			return API.OK(course);
		}

		if (!userAuth.getUserId().equals(AuthConstant.NOT_EXIST)) {

		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/courses/{courseId}/teachers")
	public API<CourseTeacher> getCourseTeacher(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@UserAuthInjection UserAuth userAuth,
		@PathVariable Long courseId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			List<Course> course = courseUseCase.getCourseList();
			return API.OK(courseUseCase.getCourseTeacher(courseId));
		}

		if (!userAuth.getUserId().equals(AuthConstant.NOT_EXIST)) {

		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/courses/{courseId}")
	public API<Course> deleteCourse(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@UserAuthInjection UserAuth userAuth,
		@PathVariable Long courseId
	) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(courseUseCase.getCourse(courseId));
		}

		if (!userAuth.getUserId().equals(AuthConstant.NOT_EXIST)) {

		}

		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}
}
