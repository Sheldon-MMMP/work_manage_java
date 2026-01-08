package com.example.okrmanagement.controller;

import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.dto.ErrorResponse;
import com.example.okrmanagement.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        ErrorCode errorCode;
        
        // 根据异常消息匹配对应的错误码
        if (message.contains("not found")) {
            if (message.contains("Objective")) {
                errorCode = ErrorCode.OBJECTIVE_NOT_FOUND;
            } else if (message.contains("Key Result")) {
                errorCode = ErrorCode.KEY_RESULT_NOT_FOUND;
            } else if (message.contains("Task")) {
                errorCode = ErrorCode.TASK_NOT_FOUND;
            } else if (message.contains("Anniversary")) {
                errorCode = ErrorCode.ANNIVERSARY_NOT_FOUND;
            } else if (message.contains("Time record")) {
                errorCode = ErrorCode.TIME_RECORD_NOT_FOUND;
            } else {
                errorCode = ErrorCode.USER_NOT_FOUND;
            }
        } else if (message.contains("permission")) {
            errorCode = ErrorCode.NO_PERMISSION;
        } 
         else if (message.contains("Bad credentials")) {
            errorCode = ErrorCode.BAD_CREDENTIALS;
        } else {
            errorCode = ErrorCode.INVALID_PARAMETER;
        }

        if (message == null || message.trim().isEmpty()) {
            message = errorCode.getMessage();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorCode errorCode = ErrorCode.SYSTEM_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
