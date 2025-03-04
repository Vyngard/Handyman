package com.group2.handyman.service;

import com.group2.handyman.dto.request.MessageCreateDto;
import com.group2.handyman.dto.response.MessageResponseDto;
import java.util.List;

public interface MessageService {
    MessageResponseDto sendMessage(MessageCreateDto messageDto);
    List<MessageResponseDto> getMessagesBetweenUserAndWorker(Long userId, Long workerId);
    List<MessageResponseDto> getAllUserMessages(Long userId);
    List<MessageResponseDto> getAllWorkerMessages(Long workerId);
    MessageResponseDto getMessageById(Long messageId);
    void deleteMessage(Long messageId);
    List<MessageResponseDto> getRecentMessages(Long userId, Long workerId, int limit);
    List<MessageResponseDto> searchMessages(Long userId, Long workerId, String keyword);
    List<MessageResponseDto> getMessagesByDateRange(Long userId, Long workerId, String startDate, String endDate);
}
