package com.commonlibrary.common_library.common.ratelimit;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
public class RateLimiterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimitInterceptor rateLimitInterceptor(
            RateLimiterAspect rateLimiterAspect) {

        return new RateLimitInterceptor(rateLimiterAspect);
    }

    @Bean
    public WebMvcConfigurer rateLimiterConfigurer(
            RateLimitInterceptor interceptor) {

        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(interceptor);
            }
        };
    }
}