package com.std_data_mgmt.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.services.UserService;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam Optional<String> departmentId,
            @RequestParam Optional<String> supervisorId) {
        return new ResponseEntity<>(this.userService.getUsers(departmentId, supervisorId), HttpStatus.OK);
    }
}
