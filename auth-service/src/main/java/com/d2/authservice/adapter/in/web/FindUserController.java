package com.d2.authservice.adapter.in.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.FindUserUseCase;
import com.d2.authservice.model.domain.User;
import com.d2.core.api.API;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FindUserController {
	private final FindUserUseCase findUserUseCase;

	@GetMapping("auth/v1/users/me")
	public API<User> getUser() {
		return API.OK(findUserUseCase.getUser(1L));
	}

	@GetMapping("auth/v1/users/{userId}")
	public API<User> getUserByUserId(@PathVariable Long userId) {
		return API.OK(findUserUseCase.getUser(userId));
	}

	@GetMapping("auth/v1/users")
	public API<List<User>> getUserList() {
		return API.OK(findUserUseCase.getUserList());
	}

}
