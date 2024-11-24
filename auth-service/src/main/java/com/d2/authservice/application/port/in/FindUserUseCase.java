package com.d2.authservice.application.port.in;

import java.util.List;

import com.d2.authservice.model.domain.User;

public interface FindUserUseCase {
	User getUser(Long user);

	List<User> getUserList();
}
