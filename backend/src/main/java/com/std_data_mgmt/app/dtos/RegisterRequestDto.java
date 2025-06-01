package com.std_data_mgmt.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private String publicKey;
    private String departmentId;
    @JsonProperty(required = false)
    private String supervisorId;
}