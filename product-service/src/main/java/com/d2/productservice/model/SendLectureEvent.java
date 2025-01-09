package com.d2.productservice.model;

import com.d2.productservice.model.enums.LectureEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendLectureEvent {
	private Long lectureId;
	private LectureEvent lectureEvent;
}
