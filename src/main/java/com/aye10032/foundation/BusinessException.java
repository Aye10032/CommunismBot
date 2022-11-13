package com.aye10032.foundation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private int code;
    private String message;

    public BusinessException(int code, String message) {
        super("code: " + code + ", message: " + message);
    }

    public static void throwException(BusinessExceptionEnum businessExceptionEnum, String... args) {
        throw new BusinessException(businessExceptionEnum.getCode(), String.format(businessExceptionEnum.getMessage(), (Object[]) args));
    }

}
