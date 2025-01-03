package com.d2.core.kafka;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.constant.KafkaConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.utils.LocalThreadHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class InternalKafkaProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	public void sendAsyncTask(String topic, Object value) {
		try {
			String payload = objectMapper.writeValueAsString(value);
			MessageBuilder<String> builder = MessageBuilder
				.withPayload(payload)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaConstant.RETRY_COUNT, KafkaConstant.RETRY_COUNT_DEFAULT_VALUE);

			if (LocalThreadHelper.getRequestUUID() != null) {
				builder.setHeader(HeaderConstant.X_D2_REQUEST_UUID, LocalThreadHelper.getRequestUUID());
			}

			if (LocalThreadHelper.getRole() != null && LocalThreadHelper.getAuthDetail() != null) {
				builder.setHeader(HeaderConstant.X_D2_AUTH_ROLE, LocalThreadHelper.getRole());
				builder.setHeader(HeaderConstant.X_D2_AUTH_DETAIL, LocalThreadHelper.getAuthDetail());
			}

			log.info("kafka-send: {}", "task: %s".formatted(payload));
			kafkaTemplate.send(builder.build());
		} catch (Exception ex) {
			log.error("kafka-send: {}", ex);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "asyncTask json parsing error");
		}
	}

	public void sendAsyncTask(String topic, Object value, MessageHeaderAccessor headers) {
		try {
			String payload = objectMapper.writeValueAsString(value);
			MessageBuilder<String> builder = MessageBuilder
				.withPayload(payload);

			headers.setHeader(KafkaHeaders.TOPIC, topic);
			headers.setHeader(KafkaConstant.RETRY_COUNT, KafkaConstant.RETRY_COUNT_DEFAULT_VALUE);
			if (LocalThreadHelper.getRequestUUID() != null) {
				headers.setHeader(HeaderConstant.X_D2_REQUEST_UUID, LocalThreadHelper.getRequestUUID());
			}
			builder.setHeaders(headers);

			log.info("kafka-send: {}", "task: %s".formatted(payload));
			kafkaTemplate.send(builder.build());
		} catch (Exception ex) {
			log.error("kafka-send: {}", ex);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}
	}

	public void sendDLQ(String topic, ConsumerRecord<?, ?> record) {
		try {
			String payload = String.valueOf(record.value());

			MessageBuilder<String> builder = MessageBuilder
				.withPayload(payload)
				.setHeader(KafkaHeaders.TOPIC, topic);

			for (Header header : record.headers()) {
				builder.setHeader(header.key(), new String(header.value(), StandardCharsets.UTF_8));
			}

			log.info("kafka-send: {}", "task: %s".formatted(payload));
			kafkaTemplate.send(builder.build());
		} catch (Exception ex) {
			log.error("kafka-send: {}", ex);
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "asyncTask json parsing error");
		}
	}

}
