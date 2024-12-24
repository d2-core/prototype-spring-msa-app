package com.d2.authservice.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d2.authservice.application.port.in.VerificationUseCase;
import com.d2.authservice.application.port.out.VerificationSmsPort;
import com.d2.authservice.error.SmsAuthErrorCodeImpl;
import com.d2.authservice.model.domain.VerificationSmsCheck;
import com.d2.authservice.model.dto.VerificationSmsDto;
import com.d2.authservice.model.enums.VerificationSmsCategory;
import com.d2.core.exception.ApiExceptionImpl;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class VerificationService implements VerificationUseCase {

	private final VerificationSmsPort verificationSmsPort;

	@Override
	public void createSmsVerificationAuthCode(String phoneNumber) {

		verificationSmsPort.register(phoneNumber, randomAuthCode(),
			VerificationSmsCategory.ADMIN_USER_AUTH_SMS, false);
	}

	@Override
	public VerificationSmsCheck checkSmsVerificationCode(String phoneNumber, String authCode) {
		LocalDateTime requestDate = LocalDateTime.now();
		VerificationSmsDto verificationSmsDto;
		try {
			verificationSmsDto = verificationSmsPort.getFirstVerificationSms(phoneNumber, authCode,
				VerificationSmsCategory.ADMIN_USER_AUTH_SMS);
		} catch (ApiExceptionImpl ex) {
			throw new ApiExceptionImpl(SmsAuthErrorCodeImpl.INVALID_SMS_AUTH_CODE,
				"phoneNumber: %s, smsAuthCode: %s, smsCategory: %s, requestDate: %s".formatted(phoneNumber, authCode,
					VerificationSmsCategory.ADMIN_USER_AUTH_SMS.name(), requestDate.toString()));
		}

		Duration duration = Duration.between(verificationSmsDto.getCreatedAt(), requestDate);
		if (duration.toMinutes() >= 2.0) {
			throw new ApiExceptionImpl(SmsAuthErrorCodeImpl.TIMEOUT_SMS_AUTH_CODE,
				"createDate: %s, requestDate: %s, durationToMinutes: %s".formatted(
					verificationSmsDto.getCreatedAt().toString(), requestDate, String.valueOf(duration.toMinutes())));
		}

		verificationSmsDto = verificationSmsPort.updateVerified(verificationSmsDto.getId(),
			true);

		return VerificationSmsCheck.from(verificationSmsDto);
	}

	public String randomAuthCode() {
		Random random = new Random();
		int randomFiveDigit = 10000 + random.nextInt(90000);
		return String.valueOf(randomFiveDigit);
	}
}
