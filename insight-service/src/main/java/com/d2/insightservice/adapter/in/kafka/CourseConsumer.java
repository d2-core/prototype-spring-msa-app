package com.d2.insightservice.adapter.in.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.insightservice.application.port.in.CourseUseCase;
import com.d2.insightservice.model.request.AddCourseRatingRequest;
import com.d2.insightservice.model.request.AddCourseViewCountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CourseConsumer {

	private final CourseUseCase courseUseCase;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "insight-course-rating-added")
	public void addCourseRating(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
		try {
			AddCourseRatingRequest request = objectMapper.readValue(record.value(), AddCourseRatingRequest.class);
			courseUseCase.addCourseRating(request.getRating());

			acknowledgment.acknowledge();
		} catch (Exception ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}
	}

	@KafkaListener(topics = "insight-course-view-count-added")
	public void addCourseViewCount(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
		try {
			AddCourseViewCountRequest request = objectMapper.readValue(record.value(), AddCourseViewCountRequest.class);
			courseUseCase.addCourseViewCount(request.getViewCount());

			acknowledgment.acknowledge();
		} catch (Exception ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		}

	}
}
