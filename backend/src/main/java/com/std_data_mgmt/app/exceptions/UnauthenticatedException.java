package com.std_data_mgmt.app.exceptions;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("Access denied. User is not authenticated");
    }
}