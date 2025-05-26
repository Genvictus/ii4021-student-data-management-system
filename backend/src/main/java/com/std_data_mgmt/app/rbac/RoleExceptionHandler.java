package com.std_data_mgmt.app.rbac;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoleExceptionHandler {
    
    @ExceptionHandler(InsufficientRoleException.class)
    public ResponseEntity<String> handleInsufficientRole(InsufficientRoleException e) {
        // TODO: change string to an ErrorResponse DTO
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }
}