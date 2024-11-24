package com.d2.authservice.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.authservice.application.port.in.SmsVerificationUseCase;
import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.error.SmsAuthErrorCodeImpl;
import com.d2.authservice.model.domain.SmsVerification;
import com.d2.authservice.model.dto.SmsVerificationDto;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.exception.ApiExceptionImpl;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class SmsVerificationService implements SmsVerificationUseCase {

	private final SmsVerificationPort smsVerificationPort;

	@Override
	public SmsVerification authenticateSmsWhenRegisterAdminUser(String phoneNumber) {
		return authenticateSms(phoneNumber, SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS);
	}

	public SmsVerification checkSmsAuthCodeWhenRegisterAdminUser(String phoneNumber, String phoneAuthCode,
		LocalDateTime requestDate) {
		return checkSmsAuthCode(phoneNumber, phoneAuthCode, SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS, requestDate);
	}

	@Override
	public SmsVerification authenticateSmsWhenRegisterUser(String phoneNumber) {
		return authenticateSms(phoneNumber, SmsAuthenticationCategory.USER_AUTH_SMS);
	}

	@Override
	public SmsVerification checkSmsAuthCodeWhenRegisterUser(String phoneNumber, String phoneAuthCode,
		LocalDateTime requestDate
	) {
		return checkSmsAuthCode(phoneNumber, phoneAuthCode, SmsAuthenticationCategory.USER_AUTH_SMS, requestDate);
	}

	public SmsVerification authenticateSms(String phoneNumber, SmsAuthenticationCategory category) {
		SmsVerificationDto smsVerificationDto = smsVerificationPort.register(phoneNumber, randomAuthCode(), category,
			false);

		//TODO: Kafka Send
		return SmsVerification.from(smsVerificationDto);
	}

	public SmsVerification checkSmsAuthCode(String phoneNumber, String smsAuthCode, SmsAuthenticationCategory category,
		LocalDateTime requestDate) {
		SmsVerificationDto smsVerificationDto = new SmsVerificationDto();
		try {
			smsVerificationDto = smsVerificationPort.getFirstSmsVerificationWithThrow(phoneNumber, smsAuthCode,
				category);
		} catch (ApiExceptionImpl ex) {
			throw new ApiExceptionImpl(SmsAuthErrorCodeImpl.INVALID_SMS_AUTH_CODE,
				"phoneNumber: %s, smsAuthCode: %s, smsCategory: %s, requestDate: %s".formatted(phoneNumber, smsAuthCode,
					category.name(), requestDate.toString()));
		}

		Duration duration = Duration.between(smsVerificationDto.getCreatedAt(), requestDate);
		if (duration.toMinutes() >= 2.0) {
			throw new ApiExceptionImpl(SmsAuthErrorCodeImpl.TIMEOUT_SMS_AUTH_CODE,
				"createDate: %s, requestDate: %s, durationToMinutes: %s".formatted(
					smsVerificationDto.getCreatedAt().toString(), requestDate, String.valueOf(duration.toMinutes())));
		}

		SmsVerificationDto updateSmsVerificationDto = smsVerificationPort.updateVerified(smsVerificationDto.getId(),
			true);

		return SmsVerification.from(updateSmsVerificationDto);
	}

	public String randomAuthCode() {
		Random random = new Random();
		int randomFiveDigit = 10000 + random.nextInt(90000);
		return String.valueOf(randomFiveDigit);
	}
}
