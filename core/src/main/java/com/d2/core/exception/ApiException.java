package com.d2.core.exception;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import com.d2.core.api.Result;
import com.d2.core.error.ErrorCode;

public interface ApiException {
    Integer getHttpCode();

    Result getResult();

    String getReasonForServerLog();

    Object getBody();
}
