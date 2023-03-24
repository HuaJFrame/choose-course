package com.huajframe.choosecourse.config;

import com.huajframe.choosecourse.config.configbeans.LoginedInterceptor;
import com.huajframe.choosecourse.config.configbeans.NoLoginInterceptor;
import com.huajframe.choosecourse.config.configbeans.StudentArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MVC配置类
 * @author Hua JFarmer
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StudentArgumentResolver studentArgumentResolver;

    @Autowired
    private NoLoginInterceptor noLoginInterceptor;

    @Autowired
    private LoginedInterceptor loginedInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(studentArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**",
                        "/bootstrap/**",
                        "/jquery-validation/**",
                        "/js/**",
                        "/img/**",
                        "/layer/**");
        registry.addInterceptor(loginedInterceptor)
                .addPathPatterns("/login/**");
    }
}
