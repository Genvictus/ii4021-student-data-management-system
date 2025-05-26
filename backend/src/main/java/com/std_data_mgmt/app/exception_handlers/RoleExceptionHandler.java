package com.std_data_mgmt.app.exception_handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.std_data_mgmt.app.security.rbac.InsufficientRoleException;

@ControllerAdvice
public class RoleExceptionHandler {
    
    @ExceptionHandler(InsufficientRoleException.class)
    public ResponseEntity<String> handleInsufficientRole(InsufficientRoleException e) {
        // TODO: change string to an ErrorResponse DTO
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }
}