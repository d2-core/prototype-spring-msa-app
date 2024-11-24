package com.d2.authservice.adapter.out.persistence.phoneverification;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.d2.authservice.model.enums.SmsAuthenticationCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "sms_verifications")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SmsVerificationJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String phoneNumber;

	@Column(length = 50, nullable = false)
	private String smsAuthCode;

	@Column(length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private SmsAuthenticationCategory category;

	private Boolean verified;

	@CreatedDate
	private LocalDateTime createdAt;

	public SmsVerificationJpaEntity(String phoneNumber, String smsAuthCode, SmsAuthenticationCategory category,
		Boolean verified) {
		this.phoneNumber = phoneNumber;
		this.smsAuthCode = smsAuthCode;
		this.category = category;
		this.verified = verified;
	}

	public SmsVerificationJpaEntity updateVerified(Boolean verified) {
		this.verified = verified;
		return this;
	}
}
