package com.example.okrmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private int code;
    private String message;
    private Object data;

    public SuccessResponse(Object data) {
        this.code = 0;
        this.message = "success";
        this.data = data;
    }

    public SuccessResponse() {
        this.code = 0;
        this.message = "success";
        this.data = null;
    }
}
