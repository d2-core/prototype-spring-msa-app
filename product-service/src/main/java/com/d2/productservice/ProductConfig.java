package com.d2.productservice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({"com.d2.core", "com.d2.productservice"})
@Configuration
public class ProductConfig {
}
