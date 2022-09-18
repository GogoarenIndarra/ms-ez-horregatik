package com.jablonski.msezhorregatik.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
@RequiredArgsConstructor
class EncryptConfiguration {

    private final ApiConfigurationProperties config;

    @Bean
    TextEncryptor encryptor() {
        return Encryptors.delux(config.getPassword(), config.getSalt());
    }
}
