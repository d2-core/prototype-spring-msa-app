package com.d2.core.aop;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.d2.core.kafka.InternalKafkaProducer;
import com.d2.core.utils.KafkaHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class KafkaLoggerAccept {

	private final InternalKafkaProducer internalKafkaProducer;

	@Pointcut("within(com.d2..*.adapter.in.kafka..*)")
	public void kafkaConsumerMethod() {
	}

	@Before("kafkaConsumerMethod()")
	public void logBeforeKafkaConsumerMethod(JoinPoint joinPoint) {
		log.info("kafka-consumer: Called method= {}", joinPoint.getSignature());
		Arrays.stream(joinPoint.getArgs())
			.filter(arg -> arg instanceof ConsumerRecord<?, ?>)
			.map(arg -> (ConsumerRecord<String, String>)arg)
			.forEach(record -> {
				log.info("Kafka-consumer: Topic = {}, Partition = {}, Offset = {}, Key = {}, Value = {}, headers = {}",
					record.topic(),
					record.partition(),
					record.offset(),
					record.key(),
					record.value(),
					KafkaHelper.headersToMap(record.headers())
				);
			});
	}

	@AfterThrowing(pointcut = "kafkaConsumerMethod()", throwing = "exception")
	public void logException(JoinPoint joinPoint, Exception exception) {
		log.error("kafka-exception, Exception in method: {} with arguments: {}. Exception: {}",
			joinPoint.getSignature(),
			joinPoint.getArgs(),
			exception.getMessage(),
			exception);
	}
}
