package com.f1rstdigital.catalogodosabio.config.faker;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakerConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}