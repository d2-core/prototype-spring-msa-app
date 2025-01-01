package com.d2.authservice.adapter.out.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.d2.authservice.application.port.out.TeacherPort;
import com.d2.authservice.model.dto.TeacherDto;
import com.d2.authservice.model.request.TeacherRegisterRequest;
import com.d2.core.api.API;
import com.d2.core.constant.HeaderConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.AdminUserAuth;
import com.d2.core.remote.InternalRestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TeacherServiceAdapter implements TeacherPort {
	@Value("${url.product}")
	private String productUrl;

	private final InternalRestTemplate internalRestTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void register(AdminUserAuth adminUserAuth, String name, String email, String phoneNumber) {
		String url = productUrl + "/api/product/v1/teachers";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.set(HeaderConstant.X_D2_AUTH_DETAIL, objectMapper.writeValueAsString(adminUserAuth));
			internalRestTemplate.post(url, headers, new TeacherRegisterRequest(name, email, phoneNumber),
				new TypeReference<API<TeacherDto>>() {
				});
		} catch (Exception e) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, e, "");
		}
	}
}
