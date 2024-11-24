package com.d2.authservice.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.application.service.SmsVerificationService;
import com.d2.authservice.error.SmsAuthErrorCodeImpl;
import com.d2.authservice.model.domain.SmsVerification;
import com.d2.authservice.model.dto.SmsVerificationDto;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.exception.ApiExceptionImpl;

class SmsVerificationServiceTest extends AuthTestConfig {

	@Mock
	SmsVerificationPort smsVerificationPort;

	@Spy
	@InjectMocks
	SmsVerificationService smsVerificationService;

	@Test
	@DisplayName("SMS 인증 코드 발송 - 성공 검증")
	public void authenticateSms() {
		//Given
		Long id = 1L;
		String phoneNumber = "01012341234";
		String randomAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		LocalDateTime createdAt = LocalDateTime.now();

		SmsVerificationDto mockSmsVerificationDto = new SmsVerificationDto(
			id,
			phoneNumber,
			randomAuthCode,
			false,
			createdAt
		);
		//When
		when(smsVerificationService.randomAuthCode()).thenReturn(randomAuthCode);

		when(smsVerificationPort.register(eq(phoneNumber), eq(randomAuthCode), eq(category), eq(false)))
			.thenReturn(mockSmsVerificationDto);

		//Then
		SmsVerification result = smsVerificationService.authenticateSms(phoneNumber, category);

		Assertions.assertEquals(result.getPhoneNumber(), mockSmsVerificationDto.getPhoneNumber());
		Assertions.assertEquals(result.getVerificationCode(), mockSmsVerificationDto.getVerificationCode());
	}

	@Test
	@DisplayName("SMS 인증 코드 확인 - 인증 코드 매칭이 실패 했을때 검증")
	public void checkSmsAuthCode_1() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;

		//When
		when(smsVerificationPort.getFirstSmsVerificationWithThrow(eq(phoneNumber), eq(smsAuthCode),
			eq(category))).thenThrow(ApiExceptionImpl.class);

		//Then
		ApiExceptionImpl exception = Assertions.assertThrows(ApiExceptionImpl.class,
			() -> smsVerificationService.checkSmsAuthCode(phoneNumber, smsAuthCode, category, LocalDateTime.now()));

		Assertions.assertEquals(exception.getResult().getCode(), SmsAuthErrorCodeImpl.INVALID_SMS_AUTH_CODE.getCode());

	}

	@Test
	@DisplayName("SMS 인증 코드 확인 - 요청 시간이 지나서 실패 검증")
	public void checkSmsAuthCode_2() {
		//Given
		Long id = 1L;
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		LocalDateTime createdAt = LocalDateTime.now().minusMinutes(2);

		SmsVerificationDto mockSmsVerificationDto = new SmsVerificationDto(id, phoneNumber, smsAuthCode, false,
			createdAt);

		//When
		when(smsVerificationPort.getFirstSmsVerificationWithThrow(eq(phoneNumber), eq(smsAuthCode),
			eq(category))).thenReturn(mockSmsVerificationDto);

		//Then
		ApiExceptionImpl exception = Assertions.assertThrows(ApiExceptionImpl.class,
			() -> smsVerificationService.checkSmsAuthCode(phoneNumber, smsAuthCode, category, LocalDateTime.now()));

		Assertions.assertEquals(exception.getResult().getCode(), SmsAuthErrorCodeImpl.TIMEOUT_SMS_AUTH_CODE.getCode());
	}

	@Test
	@DisplayName("SMS 인증 코드 확인 - 성공 검증")
	public void checkSmsAuthCode_3() {
		// Given
		Long id = 1L;
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		LocalDateTime createdAt = LocalDateTime.now();

		SmsVerificationDto mockSmsVerificationDto = new SmsVerificationDto(id, phoneNumber, smsAuthCode, false,
			createdAt);

		SmsVerificationDto mockUpdateSmsVerificationDto = new SmsVerificationDto(id, phoneNumber, smsAuthCode, true,
			createdAt);

		//When
		when(smsVerificationPort.getFirstSmsVerificationWithThrow(eq(phoneNumber), eq(smsAuthCode),
			eq(category))).thenReturn(mockSmsVerificationDto);

		when(smsVerificationPort.updateVerified(eq(id), eq(true))).thenReturn(mockUpdateSmsVerificationDto);

		// When
		SmsVerification result = smsVerificationService.checkSmsAuthCode(phoneNumber, smsAuthCode, category,
			LocalDateTime.now());

		// Then
		Assertions.assertNotNull(result);
		Assertions.assertEquals(mockUpdateSmsVerificationDto.getPhoneNumber(), result.getPhoneNumber());
		Assertions.assertEquals(mockUpdateSmsVerificationDto.getVerificationCode(), result.getVerificationCode());
		Assertions.assertTrue(result.getVerified());
	}

	@Test
	@DisplayName("난수 코드 5자리 검증")
	public void randomAuthCode() {
		//When & Given & Then
		String result = smsVerificationService.randomAuthCode();
		Assertions.assertEquals(result.length(), 5);

	}
}
