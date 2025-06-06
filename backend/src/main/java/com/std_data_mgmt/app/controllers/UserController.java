package com.std_data_mgmt.app.controllers;

import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.services.UserService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<UserDto> getUserById(
            @PathVariable("id") String id
    ) {
        Optional<User> foundUser = this.userService.getUserById(id);
        if (foundUser.isEmpty()) {
            return new FormattedResponseEntity<>(
                    HttpStatus.NOT_FOUND,
                    false,
                    "User with id " + id + " not found",
                    null
            );
        }

        UserDto userDto = foundUser.get().toDto(false);
        return new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "ok",
                userDto
        );
    }

    @GetMapping
    public FormattedResponseEntity<List<UserDto>> getUsers(
            @RequestParam Optional<String> departmentId,
            @RequestParam Optional<String> supervisorId
    ) {

        List<User> users = this.userService.getUsers(departmentId, supervisorId);
        List<UserDto> userDtos = users.stream()
                .map(user -> user.toDto(false))
                .toList();

        return new FormattedResponseEntity<>(
                HttpStatus.OK,
                true,
                "Users found successfully",
                userDtos
        );
    }
}
