package com.example.okrmanagement.common;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(VerificExceptionHandler.class);

    public static void handleVerificationException(BindingResult bindingResult) {
        // 判断是否有错误
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "参数错误";
            log.error("Register validation failed: {}", errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}
