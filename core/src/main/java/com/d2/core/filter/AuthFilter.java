package com.d2.core.filter;

import java.io.IOException;
import java.util.Enumeration;
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

		Enumeration<String> headerNames = httpRequest.getHeaderNames();

		RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = httpRequest.getHeader(key);
			if (key.startsWith(HeaderConstant.X_D2_PREFIX.toLowerCase())) {
				requestAttributes.setAttribute(key, value, RequestAttributes.SCOPE_REQUEST);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);

	}
}
