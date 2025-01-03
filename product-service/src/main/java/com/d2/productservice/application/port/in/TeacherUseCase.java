package com.d2.productservice.application.port.in;

import java.util.List;

import com.d2.core.model.enums.AdminUserRole;
import com.d2.productservice.model.domain.Course;
import com.d2.productservice.model.domain.Teacher;

public interface TeacherUseCase {
	Teacher registerTeacher(Long adminUserId, AdminUserRole adminUserRole);

	Teacher getTeacher(Long adminUserId);

	List<Course> getTeacherCourseList(Long teacherId);
}
