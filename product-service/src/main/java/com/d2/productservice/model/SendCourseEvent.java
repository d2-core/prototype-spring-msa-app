package com.d2.productservice.model;

import com.d2.productservice.model.enums.CourseEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCourseEvent {
	private Long courseId;
	private CourseEvent courseEvent;
}
