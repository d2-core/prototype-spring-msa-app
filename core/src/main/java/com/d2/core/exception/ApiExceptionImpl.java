package com.d2.core.exception;

import com.d2.core.api.Result;
import com.d2.core.error.ErrorCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiExceptionImpl extends RuntimeException implements ApiException {
	private Integer httpCode;
	private Result result;
	private String log;
	private Object body;

	public ApiExceptionImpl(ErrorCode errorCode, String reasonArg) {
		super();
		this.httpCode = errorCode.getHttpCode();
		this.result = Result.ERROR(errorCode, reasonArg);
		this.log = "Code: " + errorCode.getCode() + ", Reason: " + errorCode.getReason() + ": [" + reasonArg + "]";
		this.body = new Object();
	}

	public ApiExceptionImpl(ErrorCode errorCode, Object body, String reasonArg) {
		super();
		this.httpCode = errorCode.getHttpCode();
		this.result = Result.ERROR(errorCode, reasonArg);
		this.log = "Code: " + errorCode.getCode() + ", Reason: " + errorCode.getReason() + ": [" + reasonArg + "]";
		this.body = body;
	}

	public ApiExceptionImpl(ErrorCode errorCode, Throwable tx, String reasonArg) {
		super();
		this.httpCode = errorCode.getHttpCode();
		this.result = Result.ERROR(errorCode, reasonArg);
		this.log = "Code: " + errorCode.getCode() + ", Reason: " + tx.getLocalizedMessage() + ": [" + reasonArg + "]";
		this.body = new Object();
	}

	public ApiExceptionImpl(ErrorCode errorCode, Throwable tx, Object body, String reasonArg) {
		super();
		this.httpCode = errorCode.getHttpCode();
		this.result = Result.ERROR(errorCode, reasonArg);
		this.log = "Code: " + errorCode.getCode() + ", Reason: " + tx.getLocalizedMessage() + ": [" + reasonArg + "]";
		this.body = body;
	}
}
