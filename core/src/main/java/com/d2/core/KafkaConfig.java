package com.d2.core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.d2.core.constant.KafkaConstant;
import com.d2.core.context.RequestScopeContext;
import com.d2.core.kafka.InternalKafkaProducer;

@EnableKafka
@Configuration
public class KafkaConfig {
	@Bean
	public KafkaAdmin.NewTopics createTopics() {
		List<String> originalTopics = List.of(
			KafkaConstant.ADMIN_USER_SIGNUP_TOPIC,
			KafkaConstant.COURSE_EVENT_TOPIC
		);

		List<NewTopic> allTopics = originalTopics.stream()
			.flatMap(topic -> Stream.of(
				new NewTopic(topic, 3, (short)3),
				new NewTopic(topic + "-dlq", 3, (short)3)
			))
			.collect(Collectors.toList());

		return new KafkaAdmin.NewTopics(allTopics.toArray(NewTopic[]::new));
	}

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
				RequestScopeContext.clear();
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
