package com.d2.core.error;

public interface ErrorCode {
    Integer getHttpCode();

    String getCode();

    String getReason();

    String getMessage();
}
