package com.d2.authservice.application.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.authservice.application.port.in.AdminUserAuthUseCase;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.constant.TokenConstant;
import com.d2.authservice.error.AdminUserErrorCodeImpl;
import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserRole;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.TokenRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminUserAuthService implements AdminUserAuthUseCase {
	private final TokenPort tokenPort;
	private final AdminUserPort adminUserPort;
	private final SmsVerificationPort smsVerificationPort;

	@Override
	public AdminUserLogin signup(AdminUserRole adminUserRole, String nickname, String email, String password,
		String phoneNumber, String authCode) {

		if (!smsVerificationPort.existSmsVerification(phoneNumber, authCode,
			SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS)) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.NOT_COMPLETED_PHONE_AUTH,
				"phoneNumber: %s, authCode: %s".formatted(phoneNumber, authCode));
		}

		if (adminUserPort.existEmailOrPhoneNumber(email, phoneNumber)) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.EXIST_EMAIL_OR_PHONE_NUMBER,
				"email: %s".formatted(email));
		}

		LocalDateTime lastLoginAt = LocalDateTime.now();
		AdminUserDto adminUserDto = adminUserPort.register(adminUserRole, nickname, email, password, phoneNumber,
			AdminUserStatus.REGISTERED, lastLoginAt);

		Map<String, Object> data = getAdminUserClaimData(adminUserDto.getId());
		TokenDto accessTokenDto = tokenPort.issueAccessToken(data);

		TokenDto refreshTokenDto = tokenPort.issueRefreshToken(data);
		return AdminUserLogin.from(adminUserDto, accessTokenDto, refreshTokenDto);
	}

	@Override
	public AdminUserLogin login(String email, String password) {
		AdminUserDto adminUserDto;

		try {
			adminUserDto = adminUserPort.getAdminUserByEmailAndPasswordWithThrow(email, password);
		} catch (ApiExceptionImpl e) {
			throw new ApiExceptionImpl(AdminUserErrorCodeImpl.EMAIL_OR_PASSWORD_NOT_MATCHED, e,
				"email: %s, password: %s".formatted(email, password));
		}

		Map<String, Object> data = getAdminUserClaimData(adminUserDto.getId());

		TokenDto accessTokenDto = tokenPort.issueAccessToken(data);
		TokenDto refreshTokenDto = tokenPort.issueRefreshToken(data);

		return AdminUserLogin.from(adminUserDto, accessTokenDto, refreshTokenDto);
	}

	public Map<String, Object> getAdminUserClaimData(Long adminUserId) {
		return Map.of(
			TokenConstant.ROLE, TokenRole.ADMIN.name(),
			TokenConstant.ID, String.valueOf(adminUserId)
		);
	}
}
