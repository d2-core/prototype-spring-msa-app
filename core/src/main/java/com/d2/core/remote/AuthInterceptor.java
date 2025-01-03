package com.d2.core.remote;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.context.RequestScopeContext;

@Component
public class AuthInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
		IOException {
		HttpHeaders headers = request.getHeaders();
		Set<String> d2PrefixHeaderKeys = headers.keySet().stream()
			.filter(key -> key.startsWith(HeaderConstant.X_D2_PREFIX))
			.collect(Collectors.toSet());

		d2PrefixHeaderKeys.forEach(key -> {
			String value = String.valueOf(RequestScopeContext.getAttribute(key));
			headers.add(key, value);
		});

		return execution.execute(request, body);
	}
}
