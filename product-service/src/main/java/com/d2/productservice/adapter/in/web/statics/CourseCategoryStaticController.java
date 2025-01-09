package com.d2.productservice.adapter.in.web.statics;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
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
import com.d2.core.model.domain.MoveOrder;
import com.d2.core.resolver.AdminUserAuthInjection;
import com.d2.productservice.application.port.in.CourseStaticUseCase;
import com.d2.productservice.model.domain.CourseCategory;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;
import com.d2.productservice.model.request.StaticMoveRequest;
import com.d2.productservice.model.request.StaticUpsertRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class CourseCategoryStaticController {
	private final CourseStaticUseCase courseStaticUseCase;

	@PostMapping("v1/statics/course/categories")
	public API<CourseCategory> registerCourseCategory(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			StaticDto staticDto = courseStaticUseCase.upsertStatic(StaticCategory.COURSE_CATEGORY,
				null,
				staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
			return API.OK(CourseCategory.from(staticDto));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PostMapping("v1/statics/course/categories/move")
	public API<List<MoveOrder>> moveCourseCategory(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticMoveRequest staticMoveRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(courseStaticUseCase.moveStatic(staticMoveRequest.getMoveOrders()));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PutMapping("v1/statics/course/categories/{courseCategoryId}")
	public API<CourseCategory> modifyCourseCategory(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseCategoryId,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			StaticDto staticDto = courseStaticUseCase.upsertStatic(StaticCategory.COURSE_CATEGORY, courseCategoryId,
				staticUpsertRequest.getName(), staticUpsertRequest.getDescription());
			return API.OK(CourseCategory.from(staticDto));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/statics/course/categories")
	public API<List<CourseCategory>> getCourseCategoryList(@AdminUserAuthInjection AdminUserAuth adminUserAuth) {

		return API.OK(courseStaticUseCase.getStaticList(StaticCategory.COURSE_CATEGORY)
			.stream().map(CourseCategory::from)
			.collect(Collectors.toList()));
	}

	@GetMapping("v1/statics/course/categories/{courseCategoryId}")
	public API<CourseCategory> getCourseCategory(@PathVariable Long courseCategoryId) {
		StaticDto staticDto = courseStaticUseCase.getStatic(courseCategoryId);
		return API.OK(CourseCategory.from(staticDto));
	}
}
