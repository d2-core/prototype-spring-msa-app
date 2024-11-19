package com.d2.core.exceptionhandler;

import com.d2.core.api.API;
import com.d2.core.api.Result;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiException;
import com.d2.core.exception.ApiExceptionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Integer.MIN_VALUE)
@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = ApiExceptionImpl.class)
    public ResponseEntity<API<Object>> handleException(ApiExceptionImpl ex) {
        logger.error(ex.getReasonForServerLog());

        return ResponseEntity
                .status(ex.getHttpCode())
                .body(API.ERROR(ex.getResult(), ex.getBody()));
    }
}
