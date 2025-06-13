package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserById(String id) {
        return this.userRepository.findById(id);
    }

    public List<User> getUsers(
            Optional<String> departmentId,
            Optional<String> supervisorId,
            Optional<Role> role
    ) {
        User probe = new User();
        departmentId.ifPresent(probe::setDepartmentId);
        supervisorId.ifPresent(probe::setSupervisorId);
        role.ifPresent(probe::setRole);

        return this.userRepository.findAll(Example.of(probe));
    }
}
