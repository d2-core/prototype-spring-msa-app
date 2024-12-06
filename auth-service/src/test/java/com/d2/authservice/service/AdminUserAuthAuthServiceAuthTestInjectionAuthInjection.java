package com.d2.authservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.application.port.out.AdminUserPort;
import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.application.port.out.TokenPort;
import com.d2.authservice.application.service.AdminUserAuthService;
import com.d2.authservice.error.AdminUserErrorCodeImpl;
import com.d2.authservice.model.domain.AdminUserLogin;
import com.d2.authservice.model.dto.AdminUserDto;
import com.d2.authservice.model.dto.TokenDto;
import com.d2.authservice.model.enums.AdminUserPermission;
import com.d2.authservice.model.enums.AdminUserStatus;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.enums.Role;

class AdminUserAuthAuthServiceAuthTestInjectionAuthInjection extends AuthTestConfig {
	@Mock
	TokenPort tokenPort;

	@Mock
	AdminUserPort adminUserPort;

	@Mock
	SmsVerificationPort smsVerificationPort;

	@InjectMocks
	AdminUserAuthService adminUserAuthService;

	@Test
	@DisplayName("회원 가입 - 기존 이메일 존재 검증")
	void register_1() {
		String email = "test@naver.com";

		when(adminUserPort.existEmail(eq(email))).thenReturn(true);

		ApiExceptionImpl exception = assertThrows(ApiExceptionImpl.class,
			() -> adminUserAuthService.register("", email, "", "", ""));

		Assertions.assertEquals(exception.getResult().getCode(), AdminUserErrorCodeImpl.EXIST_EMAIL.getCode());
	}

	@Test
	@DisplayName("회원 가입 - 기존 전화번호 존재 검증")
	void register_2() {
		String phoneNumber = "01012341234";

		when(adminUserPort.existPhoneNumber(eq(phoneNumber))).thenReturn(true);

		ApiExceptionImpl exception = assertThrows(ApiExceptionImpl.class,
			() -> adminUserAuthService.register("", "", "", phoneNumber, ""));

		Assertions.assertEquals(exception.getResult().getCode(), AdminUserErrorCodeImpl.EXIST_PHONE_NUMBER.getCode());
	}

	@Test
	@DisplayName("회원 가입 - 인증을 하지 않은 사용자 검증")
	void register_3() {
		String phoneNumber = "01012341234";
		String authCode = "12345";

		when(smsVerificationPort.existSmsVerification(eq(phoneNumber), eq(authCode),
			eq(SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS))).thenReturn(false);

		ApiExceptionImpl exception = assertThrows(ApiExceptionImpl.class,
			() -> adminUserAuthService.register("", "", "", phoneNumber, authCode));

		Assertions.assertEquals(exception.getResult().getCode(),
			AdminUserErrorCodeImpl.NOT_COMPLETED_PHONE_AUTH.getCode());
	}

	@Test
	@DisplayName("회원 가입 - 회원 가입 성공")
	void register_4() {
		String name = "d2";
		String email = "test@naver.com";
		String password = "password";
		String phoneNumber = "01012341234";
		String authCode = "12345";
		AdminUserDto mockAdminUserDto = new AdminUserDto(1L, Role.ADMIN, name, email,
			password, phoneNumber, AdminUserStatus.REGISTERED, LocalDateTime.now(), LocalDateTime.now());

		TokenDto mockAccessToken = new TokenDto("access-token", LocalDateTime.now().plusHours(1));
		TokenDto mockRefreshToken = new TokenDto("refresh-token", LocalDateTime.now().plusHours(12));

		when(adminUserPort.existEmail(eq(email))).thenReturn(false);
		when(adminUserPort.existPhoneNumber(eq(phoneNumber))).thenReturn(false);
		when(smsVerificationPort.existSmsVerification(eq(phoneNumber), eq(authCode),
			eq(SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS))).thenReturn(true);

		when(adminUserPort.register(eq(name), eq(Role.ADMIN), eq(AdminUserPermission.READ), eq(email), eq(password),
			eq(phoneNumber), eq(AdminUserStatus.REGISTERED), any(LocalDateTime.class))).thenReturn(mockAdminUserDto);

		when(tokenPort.issueAccessToken(anyMap())).thenReturn(mockAccessToken);
		when(tokenPort.issueRefreshToken(anyMap())).thenReturn(mockRefreshToken);

		AdminUserLogin result = adminUserAuthService.register(name, email, password, phoneNumber, authCode);

		Assertions.assertEquals(result.getId(), mockAdminUserDto.getId());
		Assertions.assertEquals(result.getName(), mockAdminUserDto.getName());
		Assertions.assertEquals(result.getEmail(), mockAdminUserDto.getEmail());
		Assertions.assertEquals(result.getPhoneNumber(), mockAdminUserDto.getPhoneNumber());
		Assertions.assertEquals(result.getStatus(), mockAdminUserDto.getStatus());
		Assertions.assertEquals(result.getRegisteredAt(), mockAdminUserDto.getRegisteredAt());
		Assertions.assertEquals(result.getLastLoginAt(), mockAdminUserDto.getLastLoginAt());
		Assertions.assertEquals(result.getToken().getAccessToken(), mockAccessToken.getToken());
		Assertions.assertEquals(result.getToken().getAccessExpiredAt(), mockAccessToken.getExpiredAt());
		Assertions.assertEquals(result.getToken().getRefreshToken(), mockRefreshToken.getToken());
		Assertions.assertEquals(result.getToken().getRefreshExpiredAt(), mockRefreshToken.getExpiredAt());
	}

	@Test
	@DisplayName("로그인 성공")
	void login_1() {
		String email = "d2@naver.com";
		String password = "password";

		AdminUserDto mockAdminUserDto = new AdminUserDto(1L, Role.ADMIN,
			"d2", email, password, "01012341234", AdminUserStatus.REGISTERED, LocalDateTime.now(),
			LocalDateTime.now());

		TokenDto mockAccessToken = new TokenDto("access-token", LocalDateTime.now().plusHours(1));
		TokenDto mockRefreshToken = new TokenDto("refresh-token", LocalDateTime.now().plusHours(12));

		when(adminUserPort.getAdminUserByEmailAndPasswordWithThrow(eq(email), eq(password))).thenReturn(
			mockAdminUserDto);

		when(tokenPort.issueAccessToken(anyMap())).thenReturn(mockAccessToken);
		when(tokenPort.issueRefreshToken(anyMap())).thenReturn(mockRefreshToken);

		AdminUserLogin result = adminUserAuthService.login(email, password);

		assertThat(result.getId())
			.isEqualTo(mockAdminUserDto.getId());
		assertThat(result.getId())
			.isEqualTo(mockAdminUserDto.getId());

		Assertions.assertEquals(result.getId(), mockAdminUserDto.getId());
		Assertions.assertEquals(result.getName(), mockAdminUserDto.getName());
		Assertions.assertEquals(result.getEmail(), mockAdminUserDto.getEmail());
		Assertions.assertEquals(result.getPhoneNumber(), mockAdminUserDto.getPhoneNumber());
		Assertions.assertEquals(result.getStatus(), mockAdminUserDto.getStatus());
		Assertions.assertEquals(result.getRegisteredAt(), mockAdminUserDto.getRegisteredAt());
		Assertions.assertEquals(result.getLastLoginAt(), mockAdminUserDto.getLastLoginAt());
		Assertions.assertEquals(result.getToken().getAccessToken(), mockAccessToken.getToken());
		Assertions.assertEquals(result.getToken().getAccessExpiredAt(), mockAccessToken.getExpiredAt());
		Assertions.assertEquals(result.getToken().getRefreshToken(), mockRefreshToken.getToken());
		Assertions.assertEquals(result.getToken().getRefreshExpiredAt(), mockRefreshToken.getExpiredAt());
	}

	@Test
	@DisplayName("로그인 실패 - 일치 하는 이메일 또는 패스워드가 없을때")
	void login_2() {
		String email = "d2@naver.com";
		String password = "password";

		when(adminUserPort.getAdminUserByEmailAndPasswordWithThrow(eq(email), eq(password))).thenThrow(
			new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND, ""));

		ApiExceptionImpl exception = Assertions.assertThrows(ApiExceptionImpl.class,
			() -> adminUserAuthService.login(email, password));

		Assertions.assertEquals(exception.getResult().getCode(),
			AdminUserErrorCodeImpl.EMAIL_OR_PASSWORD_NOT_MATCHED.getCode());
	}
}
