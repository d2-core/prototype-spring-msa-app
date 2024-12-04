package com.d2.core.filter;

import java.io.IOException;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.d2.core.constant.HeaderConstant;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;

		String uuid = httpRequest.getHeader(HeaderConstant.X_REQUEST_UUID);
		String role = httpRequest.getHeader(HeaderConstant.X_D2_AUTH_ROLE);
		String id = httpRequest.getHeader(HeaderConstant.X_D2_AUTH_ID);
		String authDetail = httpRequest.getHeader(HeaderConstant.X_D2_AUTH_DETAIL);

		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		requestAttributes.setAttribute(HeaderConstant.X_REQUEST_UUID, uuid, RequestAttributes.SCOPE_REQUEST);
		requestAttributes.setAttribute(HeaderConstant.X_D2_AUTH_ROLE, role, RequestAttributes.SCOPE_REQUEST);
		requestAttributes.setAttribute(HeaderConstant.X_D2_AUTH_ID, id, RequestAttributes.SCOPE_REQUEST);
		requestAttributes.setAttribute(HeaderConstant.X_D2_AUTH_DETAIL, authDetail, RequestAttributes.SCOPE_REQUEST);

		filterChain.doFilter(servletRequest, servletResponse);

	}
}
