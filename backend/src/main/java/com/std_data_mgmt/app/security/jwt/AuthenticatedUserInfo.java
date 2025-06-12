package com.std_data_mgmt.app.security.jwt;

import com.std_data_mgmt.app.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticatedUserInfo {
    private String userId;
    private String email;
    private String fullName;
    private Role role;
    private String departmentId;
}
