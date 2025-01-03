package com.d2.productservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeacherRegisterRequest {
	private String name;

	private String email;

	private String phoneNumber;

}
