package com.d2.authservice.model.domain;

import java.time.LocalDateTime;

import com.d2.authservice.model.dto.SmsVerificationDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsVerification {
	private final Long id;

	private final String phoneNumber;

	private final String verificationCode;

	private Boolean verified;

	private LocalDateTime createdAt;

	public static SmsVerification from(SmsVerificationDto smsVerificationDto) {
		return new SmsVerification(
			smsVerificationDto.getId(),
			smsVerificationDto.getPhoneNumber(),
			smsVerificationDto.getVerificationCode(),
			smsVerificationDto.getVerified(),
			smsVerificationDto.getCreatedAt()
		);
	}
}
