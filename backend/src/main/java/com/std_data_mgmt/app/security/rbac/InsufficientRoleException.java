package com.std_data_mgmt.app.security.rbac;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.std_data_mgmt.app.enums.Role;

import lombok.Getter;

@Getter
public class InsufficientRoleException extends RuntimeException {
    private final Role[] requiredRoles;
    private final String userRole;
    private final boolean requireAll;

    public InsufficientRoleException(Role[] requiredRoles, String userRole, boolean requireAll) {
        super(String.format(
            "Access denied. Required %s: {%s}. User has: %s",
            requireAll ? "all of these roles" : "at least one of these roles",
            Arrays.stream(requiredRoles)
                  .map(Role::toString)
                  .collect(Collectors.joining(", ")),
            userRole
        ));

        this.requiredRoles = requiredRoles;
        this.userRole = userRole;
        this.requireAll = requireAll;
    }

    public InsufficientRoleException(Role[] requiredRoles, String userRole) {
        super(String.format(
            "Access denied. Required %s: {%s}. User has: %s",
            "at least one of these roles",
            Arrays.stream(requiredRoles)
                  .map(Role::toString)
                  .collect(Collectors.joining(", ")),
            userRole
        ));
        
        this.requiredRoles = requiredRoles;
        this.userRole = userRole;
        this.requireAll = false;
    }
}