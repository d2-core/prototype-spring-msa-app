package com.d2.core.aop;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.context.RequestScopeContext;
import com.d2.core.utils.KafkaHelper;

import lombok.RequiredArgsConstructor;

@Aspect
@RequiredArgsConstructor
@Component
public class KafkaAuthAccept {
	@Pointcut("within(com.d2..*.adapter.in.kafka..*)")
	public void kafkaConsumerMethod() {
	}

	@Before("kafkaConsumerMethod()")
	public void saveHttpHeadersBeforeKafkaConsumer(JoinPoint joinPoint) {
		Arrays.stream(joinPoint.getArgs())
			.filter(arg -> arg instanceof ConsumerRecord<?, ?>)
			.map(arg -> (ConsumerRecord<String, String>)arg)
			.forEach((consumerRecord) -> {
				Map<String, String> headers = KafkaHelper.headersToMap(consumerRecord.headers());
				headers.keySet().forEach(key -> {
					if (key.startsWith(HeaderConstant.X_D2_PREFIX.toLowerCase())) {
						RequestScopeContext.setAttribute(key, headers.get(key));
					}
				});
			});
	}

	@After("kafkaConsumerMethod()")
	public void saveHttpHeadersAfterKafkaConsumer(JoinPoint joinPoint) {
		RequestScopeContext.clear();
	}
}
