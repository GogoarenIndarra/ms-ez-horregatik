package com.jablonski.msezhorregatik.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApiConfig {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
