package com.jiankun.gym.config;

import com.jiankun.gym.converter.String2DateConverter;
import com.jiankun.gym.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/15 14:29
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new String2DateConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).
                addPathPatterns("/**").
                excludePathPatterns("/admin/login", "/admin/logout", "/captcha");
    }
}
