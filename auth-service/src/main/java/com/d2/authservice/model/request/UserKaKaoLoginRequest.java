package com.d2.authservice.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserKaKaoLoginRequest {
	private String code;
}
