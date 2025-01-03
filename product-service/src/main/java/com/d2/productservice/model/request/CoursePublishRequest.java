package com.d2.productservice.model.request;

import com.d2.productservice.model.enums.CoursePublishState;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoursePublishRequest {
	private Long courseId;

	private CoursePublishState publishState;
}
