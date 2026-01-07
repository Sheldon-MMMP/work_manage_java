package com.example.okrmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
    private Object data;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
}
