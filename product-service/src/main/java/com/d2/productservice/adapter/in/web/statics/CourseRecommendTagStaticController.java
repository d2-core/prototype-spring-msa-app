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
import com.d2.productservice.model.domain.CourseRecommendTag;
import com.d2.productservice.model.dto.StaticDto;
import com.d2.productservice.model.enums.StaticCategory;
import com.d2.productservice.model.request.StaticMoveRequest;
import com.d2.productservice.model.request.StaticUpsertRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class CourseRecommendTagStaticController {
	private final CourseStaticUseCase courseStaticUseCase;

	@PostMapping("v1/statics/course/recommend-tags")
	public API<CourseRecommendTag> register(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			StaticDto staticDto = courseStaticUseCase.upsertStatic(StaticCategory.COURSE_RECOMMEND_TAG,
				null,
				staticUpsertRequest.getName(),
				staticUpsertRequest.getDescription());
			return API.OK(CourseRecommendTag.from(staticDto));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PostMapping("v1/statics/course/recommend-tags/move")
	public API<List<MoveOrder>> move(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@RequestBody StaticMoveRequest staticMoveRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(courseStaticUseCase.moveStatic(staticMoveRequest.getMoveOrders()));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@PutMapping("v1/statics/course/recommend-tags/{courseRecommendTagId}")
	public API<CourseRecommendTag> modify(
		@AdminUserAuthInjection AdminUserAuth adminUserAuth,
		@PathVariable Long courseRecommendTagId,
		@RequestBody StaticUpsertRequest staticUpsertRequest) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			StaticDto staticDto = courseStaticUseCase.upsertStatic(StaticCategory.COURSE_RECOMMEND_TAG,
				courseRecommendTagId,
				staticUpsertRequest.getName(), staticUpsertRequest.getDescription());
			return API.OK(CourseRecommendTag.from(staticDto));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/statics/course/recommend-tags")
	public API<List<CourseRecommendTag>> getList(@AdminUserAuthInjection AdminUserAuth adminUserAuth) {

		return API.OK(courseStaticUseCase.getStaticList(StaticCategory.COURSE_RECOMMEND_TAG)
			.stream().map(CourseRecommendTag::from)
			.collect(Collectors.toList()));
	}

	@GetMapping("v1/statics/course/recommend-tags/{courseRecommendTagId}")
	public API<CourseRecommendTag> getDetail(@PathVariable Long courseRecommendTagId) {
		StaticDto staticDto = courseStaticUseCase.getStatic(courseRecommendTagId);
		return API.OK(CourseRecommendTag.from(staticDto));
	}
}
