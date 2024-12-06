package com.d2.authservice.application.port.in;

import java.util.List;

import com.d2.authservice.model.domain.User;
import com.d2.authservice.model.enums.UserSortStandard;
import com.d2.core.model.domain.UserAuth;
import com.d2.core.model.dto.SortDto;

public interface FindUserUseCase {
	UserAuth getUserAuth(Long userId);

	User getUser(Long user);

	List<User> getUserList(String email, String nickname, String phoneNumber, List<SortDto<UserSortStandard>> sorts,
		Long pageNo,
		Integer pageSize);
}
