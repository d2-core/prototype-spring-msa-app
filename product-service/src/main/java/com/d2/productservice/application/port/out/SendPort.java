package com.d2.productservice.application.port.out;

import com.d2.productservice.model.SendCourseEvent;
import com.d2.productservice.model.SendLectureEvent;

public interface SendPort {
	void sendLectureEvent(SendLectureEvent sendLectureEvent);

	void sendCourseEvent(SendCourseEvent sendCourseEvent);
}
