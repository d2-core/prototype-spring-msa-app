package com.d2.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.FindUserUseCase;
import com.d2.authservice.application.port.out.UserPort;
import com.d2.authservice.model.domain.User;
import com.d2.authservice.model.dto.UserDto;
import com.d2.authservice.model.enums.UserSortStandard;
import com.d2.core.model.domain.UserAuth;
import com.d2.core.model.dto.SortDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FindUserService implements FindUserUseCase {

	private final UserPort userPort;

	@Override
	public UserAuth getUserAuth(Long userId) {
		return new UserAuth(userId);
	}

	@Override
	public User getUser(Long userId) {
		UserDto userDto = userPort.getUser(userId);
		return User.from(userDto);
	}

	@Override
	public List<User> getUserList(String email, String nickname, String phoneNumber,
		List<SortDto<UserSortStandard>> sorts, Long pageNo, Integer pageSize) {
		return userPort.getUserList(email, nickname, phoneNumber, sorts, pageNo, pageSize)
			.stream()
			.map(User::from)
			.toList();
	}
}
