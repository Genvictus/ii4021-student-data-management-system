package com.std_data_mgmt.app.exceptions.handlers;

import com.std_data_mgmt.app.exceptions.ResourceAlreadyExistsException;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public FormattedResponseEntity<Void> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        String message = e.getMessage();
//        TODO: log the error message
        return new FormattedResponseEntity<>(
                HttpStatus.BAD_REQUEST,
                false,
                message,
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public FormattedResponseEntity<Void> handleIllegalArgument(IllegalArgumentException e) {
        String message = e.getMessage();
//        TODO: log the error message
        return new FormattedResponseEntity<>(
                HttpStatus.UNPROCESSABLE_ENTITY,
                false,
                message,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FormattedResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        // TODO: Log the validation errors

        return new FormattedResponseEntity<>(
                HttpStatus.UNPROCESSABLE_ENTITY,
                false,
                "Payload validation failed",
                errors
        );
    }
}