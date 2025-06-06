package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(
            String userId,
            String email,
            String rawPassword,
            String fullName,
            Role role,
            String publicKey,
            String departmentId,
            Optional<String> supervisorId
    ) throws IllegalArgumentException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        if (userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User with this ID already exists.");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setFullName(fullName);
        user.setRole(role);
        user.setPublicKey(publicKey);
        user.setDepartmentId(departmentId);
        supervisorId.ifPresent(user::setSupervisorId);

        this.userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}