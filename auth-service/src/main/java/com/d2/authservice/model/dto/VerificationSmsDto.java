package com.d2.authservice.model.dto;

import java.time.LocalDateTime;

import com.d2.authservice.adapter.out.persistence.phoneverification.VerificationSmsJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationSmsDto {
	private Long id;

	private String phoneNumber;

	private String verificationCode;

	private Boolean verified;

	private LocalDateTime createdAt;

	public static VerificationSmsDto from(VerificationSmsJpaEntity verificationSmsJpaEntity) {
		return new VerificationSmsDto(
			verificationSmsJpaEntity.getId(),
			verificationSmsJpaEntity.getPhoneNumber(),
			verificationSmsJpaEntity.getAuthCode(),
			verificationSmsJpaEntity.getVerified(),
			verificationSmsJpaEntity.getCreatedAt()
		);
	}
}
