package com.d2.authservice.application.port.out;

import com.d2.authservice.model.dto.VerificationSmsDto;
import com.d2.authservice.model.enums.VerificationSmsCategory;

public interface VerificationSmsPort {
	VerificationSmsDto register(String phoneNumber, String verificationCode, VerificationSmsCategory category,
		Boolean verified);

	Boolean verifySms(Long checkAuthCodeId);

	VerificationSmsDto getFirstVerificationSms(String phoneNumber, String phoneAuthCode,
		VerificationSmsCategory category);

	VerificationSmsDto updateVerified(Long id, Boolean verified);
}
