package com.d2.authservice.model.dto;

import java.time.LocalDateTime;

import com.d2.authservice.adapter.out.persistence.phoneverification.SmsVerificationJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerificationDto {
	private Long id;

	private String phoneNumber;

	private String verificationCode;

	private Boolean verified;

	private LocalDateTime createdAt;

	public static SmsVerificationDto from(SmsVerificationJpaEntity smsVerificationJpaEntity) {
		return new SmsVerificationDto(
			smsVerificationJpaEntity.getId(),
			smsVerificationJpaEntity.getPhoneNumber(),
			smsVerificationJpaEntity.getSmsAuthCode(),
			smsVerificationJpaEntity.getVerified(),
			smsVerificationJpaEntity.getCreatedAt()
		);
	}
}
