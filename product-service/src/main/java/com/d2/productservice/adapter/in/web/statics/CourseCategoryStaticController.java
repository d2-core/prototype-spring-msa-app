package com.d2.productservice.adapter.in.web.statics;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.domain.MoveOrder;
import com.d2.core.resolver.AdminUserAuthInjection;
import com.d2.productservice.application.port.in.CourseCategoryStaticUseCase;
import com.d2.productservice.model.domain.CourseCategory;
import com.d2.productservice.model.request.StaticMoveRequest;
import com.d2.productservice.model.request.StaticUpsertRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class CourseCategoryStaticController {
	private final CourseCategoryStaticUseCase courseCategoryStaticUseCase;

	@PostMapping("v1/statics/course-categories")
	public CourseCategory registerCourseCategory(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseCategoryStaticUseCase.upsertCourseCategory(null,
				staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@PostMapping("v1/statics/course-categories/move")
	public List<MoveOrder> moveCourseCategory(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		StaticMoveRequest staticMoveRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseCategoryStaticUseCase.moveCourseCategory(staticMoveRequest.getMoveOrders());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@PutMapping("v1/statics/course-categories/{courseCategoryId}")
	public CourseCategory modifyCourseCategory(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseCategoryId, @RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseCategoryStaticUseCase.upsertCourseCategory(courseCategoryId, staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@GetMapping("v1/statics/course-categories")
	public List<CourseCategory> getCourseCategoryList() {
		return courseCategoryStaticUseCase.getCourseCategoryList();
	}

	@GetMapping("v1/statics/course-categories/{courseCategoryId}")
	public CourseCategory getCourseCategory(@PathVariable Long courseCategoryId) {
		return courseCategoryStaticUseCase.getCourseCategory(courseCategoryId);
	}
}
