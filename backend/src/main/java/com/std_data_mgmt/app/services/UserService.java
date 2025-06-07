package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(String id) {
        return this.userRepository.findById(id);
    }

    public List<User> getUsers(Optional<String> departmentId, Optional<String> supervisorId) {
        User probe = new User();
        departmentId.ifPresent(probe::setDepartmentId);
        supervisorId.ifPresent(probe::setSupervisorId);

        return this.userRepository.findAll(Example.of(probe));
    }
}
