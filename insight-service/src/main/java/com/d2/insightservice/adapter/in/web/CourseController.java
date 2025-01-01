package com.d2.insightservice.adapter.in.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d2.core.api.API;
import com.d2.core.kafka.InternalKafkaProducer;
import com.d2.insightservice.application.port.in.CourseUseCase;
import com.d2.insightservice.model.domain.CourseStatics;
import com.d2.insightservice.model.enums.InsightSort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/insight")
public class CourseController {
	private final CourseUseCase courseUseCase;

	private final InternalKafkaProducer internalKafkaProducer;

	@GetMapping("test")
	public String test() {
		internalKafkaProducer.sendAsyncTask("test", "Hello");
		return "Hello";
	}

	@GetMapping("v1/courses/{insightSort}/ids")
	public API<List<Long>> getCourseIdsToPopular(@PathVariable InsightSort insightSort) {
		return API.OK(courseUseCase.getCourseIds(insightSort));
	}

	@GetMapping("v1/courses/{courseId}/statics")
	public API<CourseStatics> getCourseStatics(@PathVariable Long courseId) {
		return API.OK(courseUseCase.getCourseStatics(courseId));
	}

	@GetMapping("v1/courses/{courseId}/statics/rating")
	public API<Map<String, Double>> getCourseStaticsRating(@PathVariable Long courseId) {
		Map<String, Double> map = new HashMap<>();
		map.put("rating", courseUseCase.getCourseStaticsRating(courseId));
		return API.OK(map);
	}
}
