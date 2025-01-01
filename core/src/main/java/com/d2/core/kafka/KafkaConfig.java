package com.d2.core.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
public class KafkaConfig {

	private final InternalKafkaProducer internalKafkaProducer;

	public KafkaConfig(InternalKafkaProducer internalKafkaProducer) {
		this.internalKafkaProducer = internalKafkaProducer;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
		ConsumerFactory<String, String> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(
			(record, exception) -> {
				internalKafkaProducer.sendDLQ(record.topic() + "-dlq", record);
			},
			new FixedBackOff(300L, 3)
		);
		errorHandler.addRetryableExceptions(Exception.class);
		errorHandler.setCommitRecovered(true);

		factory.setCommonErrorHandler(errorHandler);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		return factory;
	}
}
