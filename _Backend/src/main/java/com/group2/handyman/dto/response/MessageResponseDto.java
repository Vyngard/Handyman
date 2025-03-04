package com.group2.handyman.dto.response;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private Long workerId;
    private String workerName;
    private String content;
    private LocalDateTime timestamp;

    public MessageResponseDto() {}

    public MessageResponseDto(Long id, Long userId, String username, Long workerId, 
                            String workerName, String content, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.workerId = workerId;
        this.workerName = workerName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
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
