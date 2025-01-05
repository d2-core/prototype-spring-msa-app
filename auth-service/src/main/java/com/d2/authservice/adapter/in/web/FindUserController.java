package com.d2.authservice.adapter.in.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.FindUserUseCase;
import com.d2.authservice.model.domain.User;
import com.d2.authservice.model.enums.UserSortStandard;
import com.d2.core.api.API;
import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.UserAuth;
import com.d2.core.model.dto.SortDto;
import com.d2.core.resolver.UserAuthInjection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class FindUserController {
	private final FindUserUseCase findUserUseCase;

	@GetMapping("v1/users/{userId}/auth")
	public API<UserAuth> getUserAuth(@PathVariable Long userId) {
		return API.OK(findUserUseCase.getUserAuth(userId));
	}

	@GetMapping("v1/users/me")
	public API<User> getUser(@UserAuthInjection UserAuth userAuth) {
		if (!userAuth.getUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(findUserUseCase.getUser(userAuth.getUserId()));
		}
		throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED);
	}

	@GetMapping("v1/users/{userId}")
	public API<User> getUserByUserId(@PathVariable Long userId) {
		return API.OK(findUserUseCase.getUser(userId));
	}

	@GetMapping("v1/users")
	public API<List<User>> getUserList(
		@RequestParam(required = false) String email,
		@RequestParam(required = false) String nickname,
		@RequestParam(required = false) String phoneNumber,
		@RequestParam(required = false) List<String> sorts,
		@RequestParam(required = false, defaultValue = "0") Long pageNo,
		@RequestParam(required = false, defaultValue = "10") Integer pageSize
	) {
		List<SortDto<UserSortStandard>> mapSorts = new ArrayList<>();
		if (sorts != null && !sorts.isEmpty()) {
			try {
				mapSorts = sorts.stream()
					.map(it -> {
						String[] split = it.split(",");
						return new SortDto<>(
							UserSortStandard.valueOf(split[0]),
							SortDto.Order.valueOf(split[1])
						);
					})
					.toList();
			} catch (Exception e) {
				throw new ApiExceptionImpl(ErrorCodeImpl.INVALID,
					"sorts: %s".formatted(Arrays.toString(sorts.toArray())));
			}
		}
		return API.OK(findUserUseCase.getUserList(email, nickname, phoneNumber, mapSorts, pageNo, pageSize));
	}

}
