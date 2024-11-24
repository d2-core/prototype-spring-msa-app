package com.d2.authservice.application.port.out;

import com.d2.authservice.model.dto.SmsVerificationDto;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;

public interface SmsVerificationPort {
	SmsVerificationDto register(String phoneNumber, String verificationCode, SmsAuthenticationCategory category,
		Boolean verified);

	Boolean existSmsVerification(String phoneNumber, String phoneAuthCode, SmsAuthenticationCategory category);

	SmsVerificationDto getFirstSmsVerificationWithThrow(String phoneNumber, String phoneAuthCode,
		SmsAuthenticationCategory category);

	SmsVerificationDto updateVerified(Long id, Boolean verified);
}
