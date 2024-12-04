package com.d2.core.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.d2.core.constant.AuthConstant;
import com.d2.core.constant.HeaderConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.UserAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserAuthResolver implements HandlerMethodArgumentResolver {
	private final ObjectMapper objectMapper;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Boolean annotation = parameter.hasParameterAnnotation(UserAuthInjection.class);
		Boolean parameterType = parameter.getParameterType().equals(UserAuth.class);

		return annotation && parameterType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String detail = webRequest.getHeader(HeaderConstant.X_D2_AUTH_DETAIL);

		if (detail == null) {
			return new UserAuth(AuthConstant.NOT_EXIST);
		}

		try {
			UserAuth userAuth = objectMapper.readValue(detail, UserAuth.class);
			return userAuth.getUserId() != null
				? userAuth
				: new UserAuth(AuthConstant.NOT_EXIST);
		} catch (JsonProcessingException ex) {
			return new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex, "response: %s".formatted(detail));
		}
	}
}
