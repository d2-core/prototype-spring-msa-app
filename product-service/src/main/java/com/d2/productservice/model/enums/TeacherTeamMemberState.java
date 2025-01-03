package com.d2.productservice.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TeacherTeamMemberState {
	OWNER("선생님 입니다."),
	TEAM_MEMBER("선생님의 팀원 입니다.");
	private final String description;
}
