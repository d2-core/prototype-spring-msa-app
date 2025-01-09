package com.d2.productservice.adapter.out.persistence.lecture;

import org.springframework.stereotype.Component;

import com.d2.core.adapter.out.converter.JsonArrayConverter;
import com.d2.productservice.model.dto.LectureTimelineDto;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class LectureTimelineArrayConverter extends JsonArrayConverter<LectureTimelineDto> {
	public LectureTimelineArrayConverter() {
		super(new TypeReference<>() {
		});
	}
}
