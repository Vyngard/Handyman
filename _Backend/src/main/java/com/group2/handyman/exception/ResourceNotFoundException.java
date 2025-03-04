package com.group2.handyman.exception;

public class ResourceNotFoundException extends HandymanException {
    public ResourceNotFoundException(String resource, String identifier) {
        super("NOT_FOUND", String.format("%s not found with identifier: %s", resource, identifier));
    }
}
