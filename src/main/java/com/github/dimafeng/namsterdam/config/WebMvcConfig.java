package com.github.dimafeng.namsterdam.config;

import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ViewResolver viewResolver() {

        JtwigViewResolver viewResolver = new JtwigViewResolver();
        viewResolver.setEncoding("UTF8");
        viewResolver.setPrefix("classpath:/views/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/public/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/public/images/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/public/js/");
        registry.addResourceHandler("/html/**")
                .addResourceLocations("classpath:/public/html/");
    }
}
