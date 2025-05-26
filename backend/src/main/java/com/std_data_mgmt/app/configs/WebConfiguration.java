package com.std_data_mgmt.app.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.std_data_mgmt.app.security.rbac.RoleAuthorizationInterceptor;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    
    @Autowired
    private RoleAuthorizationInterceptor roleAuthorizationInterceptor;
    
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(roleAuthorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/auth/**");
    }
}