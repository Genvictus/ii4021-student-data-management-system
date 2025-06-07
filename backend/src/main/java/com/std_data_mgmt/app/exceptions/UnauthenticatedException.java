package com.std_data_mgmt.app.exceptions;

import lombok.Getter;

@Getter
public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {
        super("Access denied. User is not authenticated");
    }
}