package com.github.dimafeng.namsterdam.config;

import com.github.dimafeng.namsterdam.controller.CustomAtomViewer;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${view.cache:true}")
    private boolean cacheViews;

    @Bean
    public ViewResolver viewResolver() {
        JtwigViewResolver viewResolver = new JtwigViewResolver();
        viewResolver.setEncoding("UTF8");
        viewResolver.setPrefix("classpath:views/");
        viewResolver.setSuffix(".html");
        viewResolver.setOrder(1);
        viewResolver.setCached(cacheViews);
        return viewResolver;
    }

    @Bean
    public ViewResolver rssViewResolver() {
        BeanNameViewResolver viewResolver = new BeanNameViewResolver();
        viewResolver.setOrder(0);
        return viewResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(50000000);
        return commonsMultipartResolver;
    }
    
    @Bean(name = "rssViewer")
    public CustomAtomViewer rssViewer() {
        return new CustomAtomViewer();
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/public/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/public/images/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/public/js/").setCachePeriod(31556926);
        registry.addResourceHandler("/html/**")
                .addResourceLocations("classpath:/public/html/");
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error404");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error404");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error500");

            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }
}
