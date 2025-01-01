package com.d2.authservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRegisterRequest {
	private String name;
	private String email;
	private String phoneNumber;
}
