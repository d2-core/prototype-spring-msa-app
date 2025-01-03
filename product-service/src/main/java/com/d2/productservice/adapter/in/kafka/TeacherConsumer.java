package com.d2.productservice.adapter.in.kafka;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.constant.KafkaConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.utils.KafkaHelper;
import com.d2.productservice.application.port.in.TeacherUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TeacherConsumer {

	private final TeacherUseCase teacherUseCase;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = KafkaConstant.ADMIN_USER_SIGNUP_TOPIC)
	public void consumeAdminUserSignup(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
		try {
			Map<String, String> headers = KafkaHelper.headersToMap(record.headers());
			AdminUserAuth adminUserAuth = objectMapper.readValue(headers.get(HeaderConstant.X_D2_AUTH_DETAIL),
				AdminUserAuth.class);
			teacherUseCase.registerTeacher(adminUserAuth.getAdminUserId(), adminUserAuth.getAdminUserRole());

			acknowledgment.acknowledge();
		} catch (JsonProcessingException e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}
	}
}
