package com.d2.core.remote;

import java.io.IOException;
import java.util.Objects;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.d2.core.constant.HeaderConstant;

@Component
public class AuthInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
		IOException {
		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());

		String uuid = String.valueOf(
			requestAttributes.getAttribute(HeaderConstant.X_REQUEST_UUID, RequestAttributes.SCOPE_REQUEST));
		request.getHeaders().add(HeaderConstant.X_D2_AUTH_ROLE, uuid);

		String role = String.valueOf(
			requestAttributes.getAttribute(HeaderConstant.X_D2_AUTH_ROLE, RequestAttributes.SCOPE_REQUEST));
		request.getHeaders().add(HeaderConstant.X_D2_AUTH_ROLE, role);

		String id = String.valueOf(
			requestAttributes.getAttribute(HeaderConstant.X_D2_AUTH_ID, RequestAttributes.SCOPE_REQUEST));
		request.getHeaders().add(HeaderConstant.X_D2_AUTH_ID, id);

		String authDetail = String.valueOf(
			requestAttributes.getAttribute(HeaderConstant.X_D2_AUTH_DETAIL, RequestAttributes.SCOPE_REQUEST));
		request.getHeaders().add(HeaderConstant.X_D2_AUTH_DETAIL, authDetail);

		return execution.execute(request, body);
	}
}
