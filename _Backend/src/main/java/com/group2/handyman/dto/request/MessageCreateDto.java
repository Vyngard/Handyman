package com.group2.handyman.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageCreateDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotBlank(message = "Message content is required")
    @Size(min = 1, max = 1000, message = "Message content must be between 1 and 1000 characters")
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
