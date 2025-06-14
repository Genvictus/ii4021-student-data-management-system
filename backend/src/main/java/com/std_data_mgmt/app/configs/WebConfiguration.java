package com.std_data_mgmt.app.configs;

import com.std_data_mgmt.app.security.authentication.AuthenticationCheckInterceptor;
import com.std_data_mgmt.app.security.rbac.RoleAuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Autowired
    private AuthenticationCheckInterceptor authenticationCheckInterceptor;

    @Autowired
    private RoleAuthorizationInterceptor roleAuthorizationInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(authenticationCheckInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/auth/**")
                .order(1);

        registry.addInterceptor(roleAuthorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/auth/**")
                .order(2);
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}