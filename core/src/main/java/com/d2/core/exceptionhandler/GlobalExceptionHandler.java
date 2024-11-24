package com.d2.core.exceptionhandler;

import java.net.BindException;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.d2.core.api.API;
import com.d2.core.api.Result;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.model.enums.ValidationDto;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({
		HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class,
		TypeMismatchException.class,
		ConstraintViolationException.class,
		BindException.class
	})
	public ResponseEntity<API<Object>> handleBadRequestExceptions(Exception ex) {
		log.error(ex.getMessage(), ex);

		Result result = Result.ERROR(ErrorCodeImpl.BAD_REQUEST);

		return ResponseEntity
			.status(ErrorCodeImpl.BAD_REQUEST.getHttpCode())
			.body(API.ERROR(result));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<API<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.error(ex.getMessage(), ex);

		List<ValidationDto> errorMessages = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> new ValidationDto(error.getField(), error.getDefaultMessage()))
			.toList();

		Result result = Result.ERROR(ErrorCodeImpl.INVALID);

		return ResponseEntity
			.status(ErrorCodeImpl.INVALID.getHttpCode())
			.body(API.ERROR(result, errorMessages));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<API<Object>> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException ex) {
		log.error(ex.getMessage(), ex);

		Result result = Result.ERROR(ErrorCodeImpl.METHOD_NOT_ALLOWED);

		return ResponseEntity
			.status(ErrorCodeImpl.METHOD_NOT_ALLOWED.getHttpCode())
			.body(API.ERROR(result));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<API<Object>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
		log.error(ex.getMessage(), ex);

		Result result = Result.ERROR(ErrorCodeImpl.UNSUPPORTED_MEDIA_TYPE);

		return ResponseEntity
			.status(ErrorCodeImpl.UNSUPPORTED_MEDIA_TYPE.getHttpCode())
			.body(API.ERROR(result));
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<API<Object>> handleException(Exception ex) {
		log.error(ex.getLocalizedMessage(), ex);

		Result result = Result.ERROR(ErrorCodeImpl.INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(ErrorCodeImpl.INTERNAL_SERVER_ERROR.getHttpCode())
			.body(API.ERROR(result));
	}
}
