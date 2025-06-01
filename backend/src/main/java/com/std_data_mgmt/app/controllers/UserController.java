package com.std_data_mgmt.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.services.UserService;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public FormattedResponseEntity<Optional<UserDto>> getUserById(
            @PathVariable("id") String id) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok", this.userService.getUserById(id));
    }

    @GetMapping
    public FormattedResponseEntity<List<UserDto>> getUsers(
            @RequestParam Optional<String> departmentId,
            @RequestParam Optional<String> supervisorId) {
        return new FormattedResponseEntity<>(HttpStatus.OK, true, "ok",
                this.userService.getUsers(departmentId, supervisorId));
    }
}
