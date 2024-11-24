package com.d2.authservice.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SmsAuthenticationRequest {
	@NotBlank
	private String phoneNumber;
}
