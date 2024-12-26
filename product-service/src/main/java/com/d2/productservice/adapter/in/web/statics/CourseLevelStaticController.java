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
import com.d2.productservice.application.port.in.CourseLevelStaticUseCase;
import com.d2.productservice.model.domain.CourseLevel;
import com.d2.productservice.model.request.StaticMoveRequest;
import com.d2.productservice.model.request.StaticUpsertRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class CourseLevelStaticController {
	private final CourseLevelStaticUseCase courseLevelStaticUseCase;

	@PostMapping("v1/statics/course-levels")
	public CourseLevel registerCourseLevel(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseLevelStaticUseCase.upsertCourseLevel(null,
				staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@PostMapping("v1/statics/course-levels/move")
	public List<MoveOrder> moveCourseLevel(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticMoveRequest staticMoveRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseLevelStaticUseCase.moveCourseLevel(staticMoveRequest.getMoveOrders());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@PutMapping("v1/statics/course-levels/{courseLevelId}")
	public CourseLevel modifyCourseLevel(@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseLevelId, @RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return courseLevelStaticUseCase.upsertCourseLevel(courseLevelId, staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@GetMapping("v1/statics/course-levels")
	public List<CourseLevel> getCourseLevelList() {
		return courseLevelStaticUseCase.getCourseLevelList();
	}

	@GetMapping("v1/statics/course-levels/{courseLevelId}")
	public CourseLevel getCourseLevel(@PathVariable Long courseLevelId) {
		return courseLevelStaticUseCase.getCourseLevel(courseLevelId);
	}
}
