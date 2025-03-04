package com.group2.handyman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class MessageDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotBlank(message = "Message content is required")
    @Size(min = 1, max = 1000, message = "Message content must be between 1 and 1000 characters")
    private String content;

    private LocalDateTime timestamp;

    // Default constructor
    public MessageDto() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with fields
    public MessageDto(Long userId, Long workerId, String content) {
        this.userId = userId;
        this.workerId = workerId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
