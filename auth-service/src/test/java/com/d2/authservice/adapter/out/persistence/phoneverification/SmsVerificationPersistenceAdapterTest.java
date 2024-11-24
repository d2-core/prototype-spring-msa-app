package com.d2.authservice.adapter.out.persistence.phoneverification;

import static com.d2.authservice.adapter.out.persistence.phoneverification.QSmsVerificationJpaEntity.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.d2.authservice.AuthTestConfig;
import com.d2.authservice.model.dto.SmsVerificationDto;
import com.d2.authservice.model.enums.SmsAuthenticationCategory;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;

class SmsVerificationPersistenceAdapterTest extends AuthTestConfig {

	@Autowired
	JPAQueryFactory queryFactory;

	@Autowired
	SmsVerificationJpaRepository smsVerificationJpaRepository;

	@Autowired
	SmsVerificationPersistenceAdapter smsVerificationPersistenceAdapter;

	@BeforeEach
	void clean() {
		smsVerificationJpaRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("등록 성공 검증")
	public void register() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		Boolean verified = false;

		//When
		SmsVerificationDto smsVerificationDto = smsVerificationPersistenceAdapter.register(phoneNumber, smsAuthCode,
			category, verified);

		//Then

		SmsVerificationJpaEntity result = queryFactory
			.selectFrom(smsVerificationJpaEntity)
			.where(smsVerificationJpaEntity.id.eq(smsVerificationDto.getId()))
			.fetchFirst();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), smsVerificationDto.getId());
		Assertions.assertEquals(result.getSmsAuthCode(), smsVerificationDto.getVerificationCode());
		Assertions.assertEquals(result.getVerified(), smsVerificationDto.getVerified());
		Assertions.assertFalse(result.getVerified());
	}

	@Test
	@DisplayName("smsVerification 존재 하지 않을때 검증")
	public void existSmsVerification_1() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;

		//When & Then
		Boolean result = smsVerificationPersistenceAdapter.existSmsVerification(phoneNumber, smsAuthCode,
			category);

		Assertions.assertFalse(result);
	}

	@Test
	@DisplayName("smsVerification 존재할때 검증")
	public void existSmsVerification_2() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		Boolean verified = false;

		//When
		SmsVerificationDto smsVerificationDto = smsVerificationPersistenceAdapter.register(phoneNumber, smsAuthCode,
			category, verified);

		//Then
		Boolean result = smsVerificationPersistenceAdapter.existSmsVerification(phoneNumber, smsAuthCode,
			category);

		Assertions.assertTrue(result);
	}

	@Test
	@DisplayName("getFirstSmsVerificationWithThrow 값 있을때 성공 검증")
	public void getFirstSmsVerificationWithThrow_1() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;
		Boolean verified = false;

		//When
		SmsVerificationDto smsVerificationDto = smsVerificationPersistenceAdapter.register(phoneNumber, smsAuthCode,
			category, verified);

		//Then
		SmsVerificationDto result = smsVerificationPersistenceAdapter.getFirstSmsVerificationWithThrow(phoneNumber,
			smsAuthCode,
			category);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getVerificationCode(), smsVerificationDto.getVerificationCode());
	}

	@Test
	@DisplayName("getFirstSmsVerificationWithThrow 값이 없을떄 검증")
	public void getFirstSmsVerificationWithThrow_2() {
		//Given
		String phoneNumber = "01012341234";
		String smsAuthCode = "12345";
		SmsAuthenticationCategory category = SmsAuthenticationCategory.ADMIN_USER_AUTH_SMS;

		//When & Then
		ApiExceptionImpl exception = Assertions.assertThrows(ApiExceptionImpl.class,
			() -> smsVerificationPersistenceAdapter.getFirstSmsVerificationWithThrow(phoneNumber, smsAuthCode,
				category));

		Assertions.assertEquals(exception.getResult().getCode(), ErrorCodeImpl.NOT_FOUND.getCode());
	}
}
