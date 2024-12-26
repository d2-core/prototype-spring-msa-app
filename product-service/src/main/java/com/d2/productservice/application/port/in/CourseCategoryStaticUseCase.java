package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;
import com.d2.productservice.model.domain.CourseCategory;

public interface CourseCategoryStaticUseCase {
	// Course Category
	CourseCategory upsertCourseCategory(Long id, String name, String description);

	List<CourseCategory> getCourseCategoryList();

	CourseCategory getCourseCategory(Long id);

	List<MoveOrder> moveCourseCategory(List<MoveOrder> moveOrders);
}
