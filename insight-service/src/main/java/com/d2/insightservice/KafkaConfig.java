package com.d2.insightservice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {
	@Bean
	public KafkaAdmin.NewTopics createTopics() {
		List<String> originalTopics = List.of(
			"test",
			"insight-course-rating-added",
			"insight-course-view-count-added"
		);

		List<NewTopic> allTopics = originalTopics.stream()
			.flatMap(topic -> Stream.of(
				new NewTopic(topic, 3, (short)3),
				new NewTopic(topic + "-dlq", 3, (short)3)
			))
			.collect(Collectors.toList());

		return new KafkaAdmin.NewTopics(allTopics.toArray(NewTopic[]::new));
	}
}
