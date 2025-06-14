package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.security.rbac.RequiresRole;
import com.std_data_mgmt.app.services.UserService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequiresRole(value = {Role.STUDENT, Role.SUPERVISOR, Role.HOD})
    @GetMapping("/{id}")
    public FormattedResponseEntity<UserDto> getUserById(
            @PathVariable("id") String id
    ) {
        Optional<User> foundUser = this.userService.getUserById(id);
        return foundUser.map(user -> new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully found user",
                user.toDto(false)
        )).orElseGet(() -> new FormattedResponseEntity<>(
                HttpStatus.NOT_FOUND,
                false,
                "User with id " + id + " not found",
                null
        ));

    }

    @RequiresRole(value = {Role.SUPERVISOR, Role.HOD})
    @GetMapping()
    public FormattedResponseEntity<List<UserDto>> getUsers(
            @RequestParam Optional<String> departmentId,
            @RequestParam Optional<String> supervisorId,
            @RequestParam Optional<String> role
    ) {

        var users = this.userService.getUsers(
                departmentId,
                supervisorId,
                role.map(Role::valueOf)
        );

        var userDtos = users.stream()
                .map(user -> user.toDto(false))
                .toList();

        return new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Successfully found users",
                userDtos
        );
    }

}
