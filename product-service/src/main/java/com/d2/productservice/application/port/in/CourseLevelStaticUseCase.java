package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.model.domain.CourseLevel;

public interface CourseLevelStaticUseCase {
	// Course Level
	CourseLevel upsertCourseLevel(Long id, String name, String description);

	List<CourseLevel> getCourseLevelList();

	CourseLevel getCourseLevel(Long id);

	List<MoveOrder> moveCourseLevel(List<MoveOrder> moveOrders);
}
