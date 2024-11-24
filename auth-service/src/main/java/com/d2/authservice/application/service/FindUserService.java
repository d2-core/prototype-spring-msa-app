package com.d2.authservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.d2.authservice.application.port.in.FindUserUseCase;
import com.d2.authservice.model.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FindUserService implements FindUserUseCase {

	@Override
	public User getUser(Long user) {
		return null;
	}

	@Override
	public List<User> getUserList() {
		return null;
	}
}
