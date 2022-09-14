package com.jablonski.msezhorregatik.registration.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    UserService userService(final UserRepository repository){
        final UserMapper mapper = new UserMapperImpl();
        return new UserService(repository, mapper);
    }
}
