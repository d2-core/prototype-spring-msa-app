package com.d2.authservice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({"com.d2.core", "com.d2.authservice"})
@Configuration
public class AuthConfig {
}
