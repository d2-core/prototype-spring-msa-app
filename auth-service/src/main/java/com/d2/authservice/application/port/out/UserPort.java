package com.d2.authservice.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

import com.d2.authservice.model.dto.UserDto;
import com.d2.authservice.model.enums.SocialCategory;
import com.d2.authservice.model.enums.UserSortStandard;
import com.d2.authservice.model.enums.UserStatus;
import com.d2.core.model.dto.SortDto;

public interface UserPort {
	UserDto upsertBySocialProfileId(String socialProfileId, String nickname, String email, String phoneNumber,
		UserStatus userStatus, LocalDateTime lastLoginAt, SocialCategory socialCategory);

	Boolean exitSocialId(String socialId);

	Boolean existEmail(String email);

	Boolean existPhoneNumber(String phoneNumber);

	UserDto getUser(Long id);

	UserDto getUserByEmail(String email);

	UserDto getUserByPhoneNumber(String phoneNumber);

	List<UserDto> getUserList(String email, String nickname, String phoneNumber, List<SortDto<UserSortStandard>> sorts,
		Long pageNo, Integer pageSize);
}
