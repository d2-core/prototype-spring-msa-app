package com.d2.authservice.application.port.in;

import java.time.LocalDateTime;

import com.d2.authservice.model.domain.SmsVerification;

public interface SmsVerificationUseCase {

	SmsVerification authenticateSmsWhenRegisterAdminUser(String phoneNumber);

	SmsVerification checkSmsAuthCodeWhenRegisterAdminUser(
		String phoneNumber,
		String phoneAuthCode,
		LocalDateTime requestDate
	);

	SmsVerification authenticateSmsWhenRegisterUser(String phoneNumber);

	SmsVerification checkSmsAuthCodeWhenRegisterUser(
		String phoneNumber,
		String phoneAuthCode,
		LocalDateTime requestDate
	);
}
