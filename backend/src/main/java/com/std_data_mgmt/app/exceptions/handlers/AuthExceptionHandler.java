package com.std_data_mgmt.app.exceptions.handlers;

import com.std_data_mgmt.app.exceptions.ForbiddenException;
import com.std_data_mgmt.app.exceptions.InsufficientRoleException;
import com.std_data_mgmt.app.exceptions.UnauthenticatedException;
import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandler {

    @ExceptionHandler({InsufficientRoleException.class, ForbiddenException.class})
    public FormattedResponseEntity<Void> handleInsufficientRoleAndForbidden(RuntimeException e) {
        String message = e.getMessage();
        // TODO: log the error message
        return new FormattedResponseEntity<>(
                HttpStatus.FORBIDDEN,
                false,
                message,
                null
        );
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public FormattedResponseEntity<Void> handleUnauthenticated(UnauthenticatedException e) {
        String message = e.getMessage();
//        TODO: log the error message
        return new FormattedResponseEntity<>(
                HttpStatus.UNAUTHORIZED,
                false,
                message,
                null
        );
    }
}