package com.d2.core;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.d2.core.resolver.AdminUserAuthResolver;
import com.d2.core.resolver.RequestUUIDResolver;
import com.d2.core.resolver.UserAuthResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CoreConfig implements WebMvcConfigurer {
	private final RequestUUIDResolver requestUUIDResolver;

	private final AdminUserAuthResolver adminUserAuthResolver;

	private final UserAuthResolver userAuthResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
		resolvers.add(requestUUIDResolver);
		resolvers.add(adminUserAuthResolver);
		resolvers.add(userAuthResolver);
	}
}
