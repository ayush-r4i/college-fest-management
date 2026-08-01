package com.collegefest.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthInterceptor())
                // Protect all server-rendered (Thymeleaf) pages...
                .addPathPatterns(
                        "/dashboard/**",
                        "/events/**",
                        "/teams/**",
                        "/participants/**",
                        "/judges/**",
                        "/scores/**",
                        "/leaderboard/**"
                )
                // ...but leave login, static assets and the REST API (for Postman) open.
                .excludePathPatterns(
                        "/login", "/logout",
                        "/css/**", "/js/**", "/images/**",
                        "/api/**"
                );
    }
}
