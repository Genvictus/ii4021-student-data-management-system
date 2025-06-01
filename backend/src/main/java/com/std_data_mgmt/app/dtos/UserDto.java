package com.std_data_mgmt.app.dtos;

import java.util.Optional;

import com.std_data_mgmt.app.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String fullName;
    private Role role;
    private String publicKey;
    private Optional<DepartmentDto> department;
    private String departmentId;
    private String supervisorId;
}
