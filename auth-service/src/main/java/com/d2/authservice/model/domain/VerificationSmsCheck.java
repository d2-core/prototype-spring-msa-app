package com.d2.authservice.model.domain;

import com.d2.authservice.model.dto.VerificationSmsDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationSmsCheck {
	private final Long verificationSmsCheckId;

	public static VerificationSmsCheck from(VerificationSmsDto verificationSmsDto) {
		return new VerificationSmsCheck(verificationSmsDto.getId());
	}
}
