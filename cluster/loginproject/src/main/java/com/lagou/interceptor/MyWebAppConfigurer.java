package com.lagou.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户登陆拦截器，排除登陆请求的路径
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**","/error");
        super.addInterceptors(registry);
    }
}
