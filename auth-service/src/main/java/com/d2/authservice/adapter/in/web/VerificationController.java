package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.VerificationUseCase;
import com.d2.authservice.model.domain.VerificationSmsCheck;
import com.d2.authservice.model.request.CheckSmsVerificationCodeRequest;
import com.d2.authservice.model.request.CreateSmsVerificationAuthCodeRequest;
import com.d2.core.api.API;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class VerificationController {
	private final VerificationUseCase verificationUseCase;

	@PostMapping("v1/verifications/sms")
	public API<Object> createSmsVerificationAuthCode(
		@RequestBody CreateSmsVerificationAuthCodeRequest createSmsVerificationAuthCodeRequest
	) {
		verificationUseCase.createSmsVerificationAuthCode(createSmsVerificationAuthCodeRequest.getPhoneNumber());

		return API.NO_CONTENT();
	}

	@PostMapping("v1/verifications/sms/check-code")
	public API<VerificationSmsCheck> checkSmsVerificationCode(
		@RequestBody CheckSmsVerificationCodeRequest checkSmsVerificationCodeRequest) {

		return API.OK(verificationUseCase.checkSmsVerificationCode(checkSmsVerificationCodeRequest.getPhoneNumber(),
			checkSmsVerificationCodeRequest.getAuthCode()));
	}
}
