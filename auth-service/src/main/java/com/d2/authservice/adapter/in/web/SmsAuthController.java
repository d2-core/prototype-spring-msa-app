package com.d2.authservice.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.d2.authservice.application.port.in.SmsVerificationUseCase;
import com.d2.authservice.model.domain.SmsVerification;
import com.d2.authservice.model.request.CheckedSmsAuthCodeRequest;
import com.d2.authservice.model.request.SmsAuthenticationRequest;
import com.d2.core.api.API;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SmsAuthController {
	private final SmsVerificationUseCase smsVerificationUseCase;

	@PostMapping("auth/v1/sms/admin-users/authenticate")
	public API<SmsVerification> authenticateSmsWhenRegisterAdminUser(
		@RequestBody SmsAuthenticationRequest smsAuthenticationRequest
	) {
		return API.OK(
			smsVerificationUseCase.authenticateSmsWhenRegisterAdminUser(smsAuthenticationRequest.getPhoneNumber()));
	}

	@PostMapping("auth/v1/sms/admin-users/check-auth-code")
	public API<SmsVerification> checkSmsAuthCodeWhenRegisterAdminUser(
		@RequestBody CheckedSmsAuthCodeRequest checkedSmsAuthCodeRequest) {
		return API.OK(
			smsVerificationUseCase.checkSmsAuthCodeWhenRegisterAdminUser(checkedSmsAuthCodeRequest.getPhoneNumber(),
				checkedSmsAuthCodeRequest.getSmsAuthCode(), checkedSmsAuthCodeRequest.getRequestDate()));
	}

	@PostMapping("auth/v1/sms/users/authenticate")
	public API<SmsVerification> authenticateSmsWhenRegisterUser(
		@RequestBody SmsAuthenticationRequest smsAuthenticationRequest
	) {
		return API.OK(
			smsVerificationUseCase.authenticateSmsWhenRegisterUser(smsAuthenticationRequest.getPhoneNumber()));
	}

	@PostMapping("auth/v1/sms/users/check-auth-code")
	public API<SmsVerification> checkSmsAuthCodeWhenRegisterUser(
		@RequestBody CheckedSmsAuthCodeRequest checkedSmsAuthCodeRequest
	) {
		return API.OK(
			smsVerificationUseCase.checkSmsAuthCodeWhenRegisterUser(checkedSmsAuthCodeRequest.getPhoneNumber(),
				checkedSmsAuthCodeRequest.getSmsAuthCode(), checkedSmsAuthCodeRequest.getRequestDate()));
	}
}
