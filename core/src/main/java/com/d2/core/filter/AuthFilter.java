package com.d2.core.filter;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

import com.d2.core.constant.HeaderConstant;
import com.d2.core.context.RequestScopeContext;

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

		try {
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				String value = httpRequest.getHeader(key);
				if (key.startsWith(HeaderConstant.X_D2_PREFIX.toLowerCase())) {
					RequestScopeContext.setAttribute(key, value);
				}
			}

			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			RequestScopeContext.clear();
		}

	}
}
