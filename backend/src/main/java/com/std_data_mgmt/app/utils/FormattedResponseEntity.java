package com.std_data_mgmt.app.utils;

import com.std_data_mgmt.app.dtos.ResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class FormattedResponseEntity<T> extends ResponseEntity<ResponseDto<T>> {

    public FormattedResponseEntity(HttpStatusCode status, boolean success, String message, T payload) {
        super(new ResponseDto<>(success, message, payload), status);
    }
}
