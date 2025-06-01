package com.std_data_mgmt.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> getUserById(String id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return Optional.of(user.get().toDto(false));
        } else {
            return Optional.of(null);
        }
    }

    public List<UserDto> getUsers(Optional<String> departmentId, Optional<String> supervisorId) {
        User probe = new User();
        departmentId.ifPresent(id -> {
            probe.setDepartmentId(id);
        });
        supervisorId.ifPresent(id -> {
            probe.setSupervisorId(id);
        });

        List<User> users = this.userRepository.findAll(Example.of(probe));
        List<UserDto> userDtos = users
                .stream()
                .map(user -> user.toDto(false))
                .collect(Collectors.toList());
        return userDtos;
    }
}
