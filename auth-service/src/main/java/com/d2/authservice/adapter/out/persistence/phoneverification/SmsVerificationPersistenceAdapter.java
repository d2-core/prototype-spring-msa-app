package com.d2.authservice.adapter.out.persistence.phoneverification;

import static com.d2.authservice.adapter.out.persistence.phoneverification.QSmsVerificationJpaEntity.*;

import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.SmsVerificationPort;
import com.d2.authservice.model.dto.SmsVerificationDto;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SmsVerificationPersistenceAdapter implements SmsVerificationPort {
	private final JPQLQueryFactory queryFactory;
	private final SmsVerificationJpaRepository smsVerificationJpaRepository;

	@Override
	public SmsVerificationDto register(String phoneNumber, String smsAuthCode, SmsAuthenticationCategory category,
		Boolean verified) {
		return SmsVerificationDto.from(smsVerificationJpaRepository.save(
			new SmsVerificationJpaEntity(phoneNumber, smsAuthCode, category, verified)));
	}

	@Override
	public Boolean existSmsVerification(String phoneNumber, String smsAuthCode, SmsAuthenticationCategory category) {
		Integer result = queryFactory
			.selectOne()
			.from(smsVerificationJpaEntity)
			.where(smsVerificationJpaEntity.phoneNumber.eq(phoneNumber),
				smsVerificationJpaEntity.smsAuthCode.eq(smsAuthCode),
				smsVerificationJpaEntity.category.eq(category))
			.fetchFirst();

		return result != null;
	}

	@Override
	public SmsVerificationDto getFirstSmsVerificationWithThrow(String phoneNumber, String smsAuthCode,
		SmsAuthenticationCategory category) {
		SmsVerificationJpaEntity smsVerification = queryFactory.selectFrom(smsVerificationJpaEntity)
			.where(smsVerificationJpaEntity.phoneNumber.eq(phoneNumber),
				smsVerificationJpaEntity.smsAuthCode.eq(smsAuthCode),
				smsVerificationJpaEntity.category.eq(category))
			.fetchFirst();

		if (smsVerification != null) {
			return SmsVerificationDto.from(smsVerification);
		} else {
			throw new ApiExceptionImpl(ErrorCodeImpl.NOT_FOUND,
				"phoneNumber: %s, smsAuthCode: %s".formatted(phoneNumber, smsAuthCode));
		}
	}

	@Override
	public SmsVerificationDto updateVerified(Long id, Boolean verified) {
		SmsVerificationJpaEntity smsVerification = queryFactory.selectFrom(smsVerificationJpaEntity)
			.where(smsVerificationJpaEntity.id.eq(id))
			.fetchFirst();

		SmsVerificationJpaEntity updateSmsVerified = smsVerification.updateVerified(verified);
		return SmsVerificationDto.from(updateSmsVerified);
	}
}
