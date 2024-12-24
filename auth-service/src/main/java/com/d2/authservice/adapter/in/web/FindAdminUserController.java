package com.d2.authservice.adapter.in.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.FindAdminUserUseCase;
import com.d2.authservice.model.domain.AdminUser;
import com.d2.authservice.model.enums.AdminUserSortStandard;
import com.d2.core.api.API;
import com.d2.core.constant.AuthConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.dto.SortDto;
import com.d2.core.resolver.AdminUserAuthInjection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class FindAdminUserController {
	private final FindAdminUserUseCase findAdminUserUseCase;

	@GetMapping("v1/admin-users/{adminUserId}/auth")
	public API<AdminUserAuth> getAdminUserAuth(@PathVariable Long adminUserId) {
		return API.OK(findAdminUserUseCase.getAdminUserAuth(adminUserId));
	}

	@GetMapping("v1/admin-users/me")
	public API<AdminUser> getAdminUser(@AdminUserAuthInjection AdminUserAuth adminUserAuth) {
		if (!adminUserAuth.getAdminUserId().equals(AuthConstant.NOT_EXIST)) {
			return API.OK(findAdminUserUseCase.getAdminUser(adminUserAuth.getAdminUserId()));
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.UNAUTHORIZED,
				"invalid id: %s".formatted(adminUserAuth.getAdminUserId()
				));
		}
	}

	@GetMapping("v1/admin-users/{adminUserId}")
	public API<AdminUser> getAdminUserByAdminUserId(@PathVariable Long adminUserId) {
		return API.OK(findAdminUserUseCase.getAdminUser(adminUserId));
	}

	@GetMapping("v1/admin-users")
	public API<List<AdminUser>> getAdminUserList(
		@RequestParam(required = false) String email,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String phoneNumber,
		@RequestParam(required = false) List<String> sorts,
		@RequestParam(required = false, defaultValue = "0") Long pageNo,
		@RequestParam(required = false, defaultValue = "10") Integer pageSize
	) {
		List<SortDto<AdminUserSortStandard>> mapSorts = new ArrayList<>();
		if (sorts != null && !sorts.isEmpty()) {
			try {
				mapSorts = sorts.stream()
					.map(it -> {
						String[] split = it.split(",");
						return new SortDto<>(
							AdminUserSortStandard.valueOf(split[0]),
							SortDto.Order.valueOf(split[1])
						);
					})
					.toList();

			} catch (Exception e) {
				throw new ApiExceptionImpl(ErrorCodeImpl.INVALID,
					"sorts: %s".formatted(Arrays.toString(sorts.toArray())));
			}
		}
		return API.OK(findAdminUserUseCase.getAdminUserList(email, name, phoneNumber, mapSorts, pageNo, pageSize));
	}
}
