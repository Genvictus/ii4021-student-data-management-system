package com.std_data_mgmt.integration.services;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.repositories.UserRepository;
import com.std_data_mgmt.app.services.AuthService;
import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("AuthService Integration Tests")
public class AuthServiceTest extends BaseIntegrationTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should authenticate existing user with valid credentials successfully")
    void successfulAuthentication() {
        // Given
        String email = "haziqabiyyu@gmail.com";
        String password = "haziqpass";

        User user = User.builder()
                .userId("13521170")
                .departmentId("135")
                .email(email)
                .password(password)
                .fullName("Haziq Abiyyu Mahdy")
                .publicKey("123456789")
                .role(Role.STUDENT)
                .build();

        authService.register(user);

        // When
        var authenticatedUser = authService.authenticate(email, password);

        // Then
        assertThat(authenticatedUser).isPresent();
    }

    @Test
    @DisplayName("Should fail to authenticate existing user with invalid credentials")
    void invalidCredentialsAuthentication() {
        // Given
        String email = "haziqabiyyu@gmail.com";
        String password = "haziqpass";

        User user = User.builder()
                .userId("13521170")
                .departmentId("135")
                .email(email)
                .password(password)
                .fullName("Haziq Abiyyu Mahdy")
                .publicKey("123456789")
                .role(Role.STUDENT)
                .build();

        authService.register(user);

        // When
        String wrongEmail = "haziq1@gmail.com";
        String wrongPassword = "haziq1";
        var authenticatedUser1 = authService.authenticate(wrongEmail, password);
        var authenticatedUser2 = authService.authenticate(email, wrongPassword);
        var authenticatedUser3 = authService.authenticate(wrongEmail, wrongPassword);

        // Then
        assertThat(authenticatedUser1).isEmpty();
        assertThat(authenticatedUser2).isEmpty();
        assertThat(authenticatedUser3).isEmpty();
    }
}

