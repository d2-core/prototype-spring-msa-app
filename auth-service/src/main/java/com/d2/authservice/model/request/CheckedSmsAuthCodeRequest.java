package com.d2.authservice.model.request;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckedSmsAuthCodeRequest {
	private String phoneNumber;

	private String smsAuthCode;

	private LocalDateTime requestDate;
}
