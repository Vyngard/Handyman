package com.group2.handyman.controller;

import com.group2.handyman.dto.request.MessageCreateDto;
import com.group2.handyman.dto.response.MessageResponseDto;
import com.group2.handyman.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "${handyman.cors.allowed-origins}")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    @PreAuthorize("@securityService.canAccessMessage(#messageDto.userId, #messageDto.workerId)")
    public ResponseEntity<MessageResponseDto> sendMessage(@Valid @RequestBody MessageCreateDto messageDto) {
        MessageResponseDto response = messageService.sendMessage(messageDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/conversation")
    @PreAuthorize("@securityService.canAccessMessage(#userId, #workerId)")
    public ResponseEntity<List<MessageResponseDto>> getConversation(
            @RequestParam Long userId,
            @RequestParam Long workerId) {
        List<MessageResponseDto> messages = messageService.getMessagesBetweenUserAndWorker(userId, workerId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<MessageResponseDto>> getUserMessages(@PathVariable Long userId) {
        List<MessageResponseDto> messages = messageService.getAllUserMessages(userId);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/worker/{workerId}")
    @PreAuthorize("@securityService.isCurrentWorker(#workerId)")
    public ResponseEntity<List<MessageResponseDto>> getWorkerMessages(@PathVariable Long workerId) {
        List<MessageResponseDto> messages = messageService.getAllWorkerMessages(workerId);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/recent")
    @PreAuthorize("@securityService.canAccessMessage(#userId, #workerId)")
    public ResponseEntity<List<MessageResponseDto>> getRecentMessages(
            @RequestParam Long userId,
            @RequestParam Long workerId,
            @RequestParam(defaultValue = "20") int limit) {
        List<MessageResponseDto> messages = messageService.getRecentMessages(userId, workerId, limit);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    @PreAuthorize("@securityService.canAccessMessage(#userId, #workerId)")
    public ResponseEntity<List<MessageResponseDto>> searchMessages(
            @RequestParam Long userId,
            @RequestParam Long workerId,
            @RequestParam String keyword) {
        List<MessageResponseDto> messages = messageService.searchMessages(userId, workerId, keyword);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("@securityService.canDeleteMessage(#messageId)")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
