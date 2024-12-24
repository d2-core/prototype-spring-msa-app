package com.d2.authservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckSmsVerificationCodeRequest {
	private String phoneNumber;

	private String authCode;
}
