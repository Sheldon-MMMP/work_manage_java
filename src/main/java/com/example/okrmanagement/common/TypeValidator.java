package com.example.okrmanagement.common;

import com.example.okrmanagement.exception.BusinessException;

public class TypeValidator {
    public static void validatePathParam(String fieldName, String value, Class<?> expectedType) {
        try {
            if (expectedType == Long.class) {
                Long.parseLong(value);
            } else if (expectedType == Integer.class) {
                Integer.parseInt(value);
            } else if (expectedType == Double.class) {
                Double.parseDouble(value);
            } else if (expectedType == Boolean.class) {
                Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            String errorMessage = String.format(ErrorCode.INVALID_FIELD_TYPE.getMessage(), fieldName, expectedType.getSimpleName(), value);
            throw new BusinessException(ErrorCode.INVALID_FIELD_TYPE.getCode(), errorMessage);
        }
    }
}
