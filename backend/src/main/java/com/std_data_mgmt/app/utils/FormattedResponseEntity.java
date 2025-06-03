package com.std_data_mgmt.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
@Setter
class ResponseFormat<T> {
    private boolean success;

    private String message;

    private T data;
}

public class FormattedResponseEntity<T> extends ResponseEntity<ResponseFormat<T>> {

    public FormattedResponseEntity(HttpStatusCode status, boolean success, String message, T payload) {
        super(new ResponseFormat<>(success, message, payload), status);
    }
}
