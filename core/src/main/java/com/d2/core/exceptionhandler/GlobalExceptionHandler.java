package com.d2.core.exceptionhandler;

import com.d2.core.api.API;
import com.d2.core.api.Result;
import com.d2.core.error.ErrorCodeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<API<Object>> handleException(Exception ex) {
        logger.error(ex.getLocalizedMessage());

        Result result = Result.ERROR(ErrorCodeImpl.INTERNAL_SERVER_ERROR);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(API.ERROR(result));
    }
}
