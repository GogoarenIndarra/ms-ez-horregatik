package com.jablonski.msezhorregatik.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "encrypt")
@Getter
@Setter
class ApiConfigurationProperties {

    private CharSequence password;
    private CharSequence salt;
}
