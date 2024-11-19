package com.d2.core.api;

import com.d2.core.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Result {
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public static Result OK() {
        Result result = new Result();
        result.code = "OK";
        result.reason = null;
        result.message = null;

        return result;
    }

    public static Result ERROR(ErrorCode errorCode) {
        Result result = new Result();
        result.code = errorCode.getCode();
        result.reason = errorCode.getReason();
        result.message = errorCode.getMessage();

        return result;
    }
}
