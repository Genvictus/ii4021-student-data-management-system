package com.std_data_mgmt.app.exceptions.handlers;

import com.std_data_mgmt.app.utils.FormattedResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public FormattedResponseEntity<Void> handleException(Exception e) {
        String message = e.getMessage();

        logger.error("GlobalExceptionHandler triggered: {} {}", e.getClass(), e.getMessage());

        return new FormattedResponseEntity<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                false,
                "Error occurred on the server",
                null
        );
    }
}