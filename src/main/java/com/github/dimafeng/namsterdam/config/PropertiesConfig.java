package com.github.dimafeng.namsterdam.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class PropertiesConfig {
    @Bean
    PropertyPlaceholderConfigurer PropertyConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setLocation(new FileSystemResource(System.getProperty("user.home") + "/namsterdam.properties"));
        return configurer;
    }
}
