package com.d2.core.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;
import com.d2.core.model.domain.RequestUUID;

@Component
public class RequestUUIDResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Boolean annotation = parameter.hasParameterAnnotation(RequestUUIDInjection.class);
		Boolean parameterType = parameter.getParameterType().equals(RequestUUID.class);

		return annotation && parameterType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String uuid = webRequest.getHeader(HeaderConstant.X_D2_REQUEST_UUID);

		if (uuid == null) {
			return new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, "uuid is null");
		}

		return new RequestUUID(uuid);
	}
}
