package com.d2.productservice.adapter.out.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.d2.core.api.API;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.remote.InternalRestTemplate;
import com.d2.productservice.application.port.out.AdminUserPort;
import com.d2.productservice.model.dto.AdminUserDto;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminUserServiceAdapter implements AdminUserPort {
	@Value("${url.auth}")
	private String authUrl;

	private final InternalRestTemplate internalRestTemplate;

	@Override
	public AdminUserDto getAdminUser(Long adminUserId) {
		try {
			String url = authUrl + "/api/auth/v1/admin-users/" + adminUserId;
			API<AdminUserDto> api = internalRestTemplate.get(url, null, new TypeReference<>() {
			});

			return api.getBody();
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e);
		}
	}
}
