package com.cnj49.propertymaintenance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Dieu huong don gian khong can logic o Controller: "/" -> dashboard, "/login" -> form dang nhap. */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/dashboard");
        registry.addViewController("/login").setViewName("auth/login");
    }
}
