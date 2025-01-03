package com.d2.authservice.adapter.out.service;

import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.SendPort;
import com.d2.authservice.model.SendAdminUserSignup;
import com.d2.core.constant.HeaderConstant;
import com.d2.core.constant.KafkaConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.kafka.InternalKafkaProducer;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.model.enums.AdminUserRole;
import com.d2.core.model.enums.TokenRole;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class KafkaSendServiceAdapter implements SendPort {
	private final InternalKafkaProducer internalKafkaProducer;
	private final ObjectMapper objectMapper;

	@Override
	public void sendAdminUserSignupEvent(Long adminUserId, TokenRole tokenRole, AdminUserRole adminUserRole) {
		AdminUserAuth adminUserAuth = new AdminUserAuth(adminUserId, tokenRole, adminUserRole);
		try {
			String adminAuthDetail = objectMapper.writeValueAsString(adminUserAuth);
			MessageHeaderAccessor headers = new MessageHeaderAccessor();
			headers.setHeader(HeaderConstant.X_D2_AUTH_ROLE, tokenRole);
			headers.setHeader(HeaderConstant.X_D2_AUTH_DETAIL, adminAuthDetail);

			SendAdminUserSignup sendAdminUserSignup = new SendAdminUserSignup(adminUserId);
			internalKafkaProducer.sendAsyncTask(KafkaConstant.ADMIN_USER_SIGNUP_TOPIC, sendAdminUserSignup, headers);
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}
	}
}
