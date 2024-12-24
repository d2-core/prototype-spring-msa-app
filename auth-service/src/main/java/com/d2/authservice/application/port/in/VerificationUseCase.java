package com.d2.authservice.application.port.in;

import com.d2.authservice.model.domain.VerificationSmsCheck;

public interface VerificationUseCase {

	void createSmsVerificationAuthCode(String phoneNumber);

	VerificationSmsCheck checkSmsVerificationCode(
		String phoneNumber,
		String phoneAuthCode
	);
}
