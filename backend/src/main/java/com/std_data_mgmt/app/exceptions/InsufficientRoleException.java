package com.std_data_mgmt.app.exceptions;

import com.std_data_mgmt.app.enums.Role;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

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
}