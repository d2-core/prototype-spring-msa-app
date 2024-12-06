package com.d2.eventservice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({"com.d2.core", "com.d2.eventservice"})
@Configuration
public class EventConfig {
}
