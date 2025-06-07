package com.std_data_mgmt.app.security.rbac;

import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.exceptions.InsufficientRoleException;
import com.std_data_mgmt.app.exceptions.UnauthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class RoleAuthorizationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RoleAuthorizationInterceptor.class);

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {

        // Only process @RequestMapping methods
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Check for @RequiresRole annotation (method level first, then class level)
        RequiresRole methodRole = handlerMethod.getMethodAnnotation(RequiresRole.class);
        RequiresRole classRole = handlerMethod.getBeanType().getAnnotation(RequiresRole.class);
        RequiresRole roleAnnotation = methodRole != null ? methodRole : classRole;

        if (roleAnnotation == null) {
            // No role requirement
            return true;
        }

        // Get current user's authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthenticatedException();
        }

        // Extract user's role
        String userRole = extractUserRole(auth);

        // Check if user has required role(s)
        boolean hasAccess = userRole != null && checkRoleAccess(roleAnnotation, userRole);

        if (!hasAccess) {
            logger.warn(
                    "Access denied for user with role {} to endpoint {}",
                    userRole, request.getRequestURI()
            );
            throw new InsufficientRoleException(roleAnnotation.value(), userRole, roleAnnotation.requireAll());
        }

        logger.debug(
                "Access granted for user with role {} to endpoint {}",
                userRole, request.getRequestURI()
        );
        return true;
    }

    private String extractUserRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .map(role -> role.substring(5)) // Remove "ROLE_" prefix
                .findFirst()
                .orElse(null);
    }

    private boolean checkRoleAccess(RequiresRole roleAnnotation, String userRole) {
        Role[] requiredRoles = roleAnnotation.value();

        try {
            Role userRoleEnum = Role.valueOf(userRole.toUpperCase());

            if (roleAnnotation.requireAll()) {
                return Arrays.stream(requiredRoles)
                        .allMatch(requiredRole -> requiredRole == userRoleEnum);
            } else {
                return Arrays.asList(requiredRoles).contains(userRoleEnum);
            }
        } catch (IllegalArgumentException e) {
            // User has a role not in the enum
            return false;
        }
    }
}