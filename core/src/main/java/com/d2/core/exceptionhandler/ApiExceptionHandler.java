package com.d2.core.exceptionhandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.d2.core.api.API;
import com.d2.core.exception.ApiExceptionImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Integer.MIN_VALUE)
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(value = ApiExceptionImpl.class)
	public ResponseEntity<API<Object>> handleException(ApiExceptionImpl ex) {
		log.error(ex.getReasonForServerLog(), ex);

		return ResponseEntity
			.status(ex.getHttpCode())
			.body(API.ERROR(ex.getResult(), ex.getBody()));
	}
}
