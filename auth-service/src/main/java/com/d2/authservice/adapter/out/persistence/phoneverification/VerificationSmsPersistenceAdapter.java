package com.d2.authservice.adapter.out.persistence.phoneverification;

import static com.d2.authservice.adapter.out.persistence.phoneverification.QVerificationSmsJpaEntity.*;

import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.VerificationSmsPort;
import com.d2.authservice.model.dto.VerificationSmsDto;
import com.d2.authservice.model.enums.VerificationSmsCategory;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VerificationSmsPersistenceAdapter implements VerificationSmsPort {
	private final JPQLQueryFactory queryFactory;
	private final VerificationSmsJpaRepository verificationSmsJpaRepository;

	@Override
	public VerificationSmsDto register(String phoneNumber, String authCode, VerificationSmsCategory category,
		Boolean verified) {
		return VerificationSmsDto.from(verificationSmsJpaRepository.save(
			new VerificationSmsJpaEntity(phoneNumber, authCode, category, verified)));
	}

	@Override
	public Boolean verifySms(Long checkAuthCodeId) {
		Boolean result = queryFactory
			.select(verificationSmsJpaEntity.verified)
			.from(verificationSmsJpaEntity)
			.where(verificationSmsJpaEntity.id.eq(checkAuthCodeId))
			.fetchFirst();

		if (result == null) {
			return false;
		}
		return result;
	}

	@Override
	public VerificationSmsDto getFirstVerificationSms(String phoneNumber, String authCode,
		VerificationSmsCategory category) {
		VerificationSmsJpaEntity smsVerification = queryFactory.selectFrom(verificationSmsJpaEntity)
			.where(verificationSmsJpaEntity.phoneNumber.eq(phoneNumber),
				verificationSmsJpaEntity.authCode.eq(authCode),
				verificationSmsJpaEntity.category.eq(category))
			.fetchFirst();

		if (smsVerification != null) {
			return VerificationSmsDto.from(smsVerification);
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND,
				"phoneNumber: %s, smsAuthCode: %s".formatted(phoneNumber, authCode));
		}
	}

	@Override
	public VerificationSmsDto updateVerified(Long id, Boolean verified) {
		VerificationSmsJpaEntity smsVerification = queryFactory.selectFrom(verificationSmsJpaEntity)
			.where(verificationSmsJpaEntity.id.eq(id))
			.fetchFirst();

		VerificationSmsJpaEntity updateSmsVerified = smsVerification.updateVerified(verified);
		return VerificationSmsDto.from(updateSmsVerified);
	}
}
