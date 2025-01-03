package com.d2.core.utils;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.context.RequestScopeContext;

public class LocalThreadHelper {
	public static String getRequestUUID() {
		Object uuid = RequestScopeContext.getAttribute(HeaderConstant.X_D2_REQUEST_UUID.toLowerCase());
		return String.valueOf(uuid);
	}

	public static String getRole() {
		Object authDetailJson = RequestScopeContext.getAttribute(HeaderConstant.X_D2_AUTH_ROLE.toLowerCase());
		return String.valueOf(authDetailJson);
	}

	public static String getAuthDetail() {
		Object authDetailJson = RequestScopeContext.getAttribute(HeaderConstant.X_D2_AUTH_DETAIL.toLowerCase());
		return String.valueOf(authDetailJson);
	}
}
