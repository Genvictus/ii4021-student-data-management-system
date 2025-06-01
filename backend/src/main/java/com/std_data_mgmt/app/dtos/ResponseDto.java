package com.std_data_mgmt.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseDto<T> {
    private boolean success;

    private String message;

    private T data;
}
