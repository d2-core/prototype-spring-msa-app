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
    private String reasonForServerLog;
    private Object body;

    public ApiExceptionImpl(ErrorCode errorCode) {
        this.httpCode = errorCode.getHttpCode();
        this.result = Result.ERROR(errorCode);
        this.reasonForServerLog = errorCode.getReason();
        this.body = new Object();
    }

    public ApiExceptionImpl(ErrorCode errorCode, Object body) {
        this.httpCode = errorCode.getHttpCode();
        this.result = Result.ERROR(errorCode);
        this.reasonForServerLog = errorCode.getReason();
        this.body = body;
    }

    public ApiExceptionImpl(ErrorCode errorCode, Throwable tx) {
        this.httpCode = errorCode.getHttpCode();
        this.result = Result.ERROR(errorCode);
        this.reasonForServerLog = tx.getLocalizedMessage();
        this.body = new Object();
    }

    public ApiExceptionImpl(ErrorCode errorCode, Throwable tx, Object body) {
        this.httpCode = errorCode.getHttpCode();
        this.result = Result.ERROR(errorCode);
        this.reasonForServerLog = tx.getLocalizedMessage();
        this.body = body;
    }
}
