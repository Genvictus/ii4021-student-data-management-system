package com.std_data_mgmt.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "User ID cannot be empty")
    @Size(min = 8, max = 50, message = "User ID must be between 3 and 50 characters")
    private String userId;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
    private String password;

    @NotBlank(message = "Full name cannot be empty")
    @Size(min = 2, max = 255, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Role cannot be empty")
    @Pattern(regexp = "^(STUDENT|SUPERVISOR|HOD)$",
            message = "Invalid role specified. Must be STUDENT, SUPERVISOR, or HOD.")
    private String role;

    @NotBlank(message = "Public key cannot be empty")
    private String publicKey;

    @NotBlank(message = "Department ID cannot be empty")
    @Size(min = 3, max = 50, message = "Department ID must be between 3 and 50 characters")
    private String departmentId;

    @JsonProperty(required = false)
    @Size(max = 50, message = "Supervisor ID cannot exceed 50 characters")
    private String supervisorId;

    public User toUser() {
        return User.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .fullName(fullName)
                .role(Role.valueOf(role.toUpperCase()))
                .publicKey(publicKey)
                .departmentId(departmentId)
                .supervisorId(supervisorId)
                .build();
    }
}