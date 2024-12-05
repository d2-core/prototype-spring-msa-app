package com.d2.core.remote;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
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
		HttpHeaders headers = request.getHeaders();
		Set<String> d2PrefixHeaderKeys = headers.keySet().stream()
			.filter(key -> key.startsWith(HeaderConstant.X_D2_PREFIX))
			.collect(Collectors.toSet());

		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		d2PrefixHeaderKeys.forEach(key -> {
			String value = String.valueOf(requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST));
			headers.add(key, value);
		});

		return execution.execute(request, body);
	}
}
