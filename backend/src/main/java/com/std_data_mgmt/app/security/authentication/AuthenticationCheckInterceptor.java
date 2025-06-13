package com.std_data_mgmt.app.security.authentication;

import com.std_data_mgmt.app.exceptions.UnauthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            // No authentication check needed for OPTIONS.
            // Spring's CORS configuration will handle setting the appropriate headers.
            return true;
        }
        
        if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                auth.getAuthorities()
                        .stream()
                        .anyMatch(authority -> "ROLE_ANONYMOUS".equals(authority.getAuthority()))
        ) {
            throw new UnauthenticatedException();
        }

        return true;
    }
}
