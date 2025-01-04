package com.d2.productservice.application.port.out;

import com.d2.productservice.model.dto.TeacherDto;
import com.d2.productservice.model.dto.TeacherTeamMemberDto;
import com.d2.productservice.model.enums.TeacherTeamMemberState;

public interface TeacherPort {

	TeacherDto registerTeacher(String name, String email, String phoneNumber);

	TeacherTeamMemberDto registerTeacherTeamMember(Long adminUserId, Long teacherId,
		TeacherTeamMemberState teacherTeamMemberState);

	TeacherDto getTeacher(Long courseId);

	TeacherDto getTeacherByAdminUserId(Long adminUserId);

	Long getTeacherIdByAdminUserId(Long adminUserId);
}
