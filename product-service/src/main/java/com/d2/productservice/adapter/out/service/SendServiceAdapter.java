package com.d2.productservice.adapter.out.service;

import org.springframework.stereotype.Component;

import com.d2.core.constant.KafkaConstant;
import com.d2.core.kafka.InternalKafkaProducer;
import com.d2.productservice.application.port.out.SendPort;
import com.d2.productservice.model.SendCourseEvent;
import com.d2.productservice.model.SendLectureEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SendServiceAdapter implements SendPort {
	private final InternalKafkaProducer kafkaProducer;

	@Override
	public void sendLectureEvent(SendLectureEvent sendLectureEvent) {
		kafkaProducer.sendAsyncTask(KafkaConstant.LECTURE_EVENT_TOPIC, sendLectureEvent);
	}

	@Override
	public void sendCourseEvent(SendCourseEvent sendCourseEvent) {
		kafkaProducer.sendAsyncTask(KafkaConstant.COURSE_EVENT_TOPIC, sendCourseEvent);
	}
}
