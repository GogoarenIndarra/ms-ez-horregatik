package com.jablonski.msezhorregatik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MsEzHorregatikApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MsEzHorregatikApplication.class, args);
    }
}
