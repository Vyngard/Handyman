package com.group2.handyman.service.impl;

import com.group2.handyman.dto.request.MessageCreateDto;
import com.group2.handyman.dto.response.MessageResponseDto;
import com.group2.handyman.exception.ResourceNotFoundException;
import com.group2.handyman.model.Message;
import com.group2.handyman.model.MessageRepository;
import com.group2.handyman.model.User;
import com.group2.handyman.model.UserRepository;
import com.group2.handyman.model.Worker;
import com.group2.handyman.model.WorkerRepository;
import com.group2.handyman.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public MessageResponseDto sendMessage(MessageCreateDto messageDto) {
        User user = userRepository.findById(messageDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", messageDto.getUserId().toString()));

        Worker worker = workerRepository.findById(messageDto.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker", messageDto.getWorkerId().toString()));

        Message message = new Message();
        message.setUser(user);
        message.setWorker(worker);
        message.setContent(messageDto.getContent());
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return toResponseDto(savedMessage);
    }

    @Override
    public List<MessageResponseDto> getMessagesBetweenUserAndWorker(Long userId, Long workerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId.toString()));

        List<Message> userToWorker = messageRepository.findByUserAndWorker(user, worker);
        List<Message> workerToUser = messageRepository.findByWorkerAndUser(worker, user);

        userToWorker.addAll(workerToUser);
        userToWorker.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));

        return userToWorker.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDto> getAllUserMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
        return messageRepository.findByUser(user).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDto> getAllWorkerMessages(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId.toString()));
        return messageRepository.findByWorker(worker).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponseDto getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", messageId.toString()));
        return toResponseDto(message);
    }

    @Override
    public void deleteMessage(Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new ResourceNotFoundException("Message", messageId.toString());
        }
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageResponseDto> getRecentMessages(Long userId, Long workerId, int limit) {
        validateUserAndWorker(userId, workerId);
        return messageRepository.findRecentMessages(userId, workerId, limit).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDto> searchMessages(Long userId, Long workerId, String keyword) {
        validateUserAndWorker(userId, workerId);
        return messageRepository.searchMessages(userId, workerId, keyword).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDto> getMessagesByDateRange(Long userId, Long workerId, String startDate, String endDate) {
        validateUserAndWorker(userId, workerId);
        
        LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return messageRepository.findByDateRange(userId, workerId, start, end).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private void validateUserAndWorker(Long userId, Long workerId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId.toString());
        }
        if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker", workerId.toString());
        }
    }

    private MessageResponseDto toResponseDto(Message message) {
        return new MessageResponseDto(
            message.getId(),
            message.getUser().getId(),
            message.getUser().getUsername(),
            message.getWorker().getId(),
            message.getWorker().getUsername(),
            message.getContent(),
            message.getTimestamp()
        );
    }
}
