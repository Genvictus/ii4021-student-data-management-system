package com.std_data_mgmt.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private String publicKey;
}