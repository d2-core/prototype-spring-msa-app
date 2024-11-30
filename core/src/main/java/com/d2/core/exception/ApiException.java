package com.d2.core.exception;

import com.d2.core.api.Result;

public interface ApiException {
	Integer getHttpCode();

	Result getResult();

	String getLog();

	Object getBody();
}
