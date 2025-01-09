package com.d2.productservice.adapter.out.persistence.video;

import org.springframework.stereotype.Component;

import com.d2.core.adapter.out.converter.JsonArrayConverter;
import com.d2.core.model.enums.VideoResolution;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class VideoResolutionListConverter extends JsonArrayConverter<VideoResolution> {
	public VideoResolutionListConverter() {
		super(new TypeReference<>() {
		});
	}
}
