package com.d2.core.utils;

import java.util.Objects;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.d2.core.constant.HeaderConstant;

public class LocalThreadHelper {
	public static String getRequestUUID() {
		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		Object uuid = requestAttributes.getAttribute(HeaderConstant.X_D2_REQUEST_UUID.toLowerCase(),
			RequestAttributes.SCOPE_REQUEST);
		return String.valueOf(uuid);
	}

	public static String getRole() {
		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		Object authDetailJson = requestAttributes.getAttribute(HeaderConstant.X_D2_AUTH_ROLE.toLowerCase(),
			RequestAttributes.SCOPE_REQUEST);
		return String.valueOf(authDetailJson);
	}

	public static String getAuthDetail() {
		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		Object authDetailJson = requestAttributes.getAttribute(HeaderConstant.X_D2_AUTH_DETAIL.toLowerCase(),
			RequestAttributes.SCOPE_REQUEST);
		return String.valueOf(authDetailJson);
	}
}
