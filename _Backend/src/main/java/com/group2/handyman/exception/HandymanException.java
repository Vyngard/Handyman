package com.group2.handyman.exception;

public class HandymanException extends RuntimeException {
    private final String code;
    private final String message;

    public HandymanException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
