package com.group2.handyman.security;

import com.group2.handyman.model.Message;
import com.group2.handyman.model.MessageRepository;
import com.group2.handyman.model.User;
import com.group2.handyman.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private MessageRepository messageRepository;

    public boolean canAccessMessage(Long userId, Long workerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        HandymanUserDetails userDetails = (HandymanUserDetails) auth.getPrincipal();
        String role = userDetails.getRole();
        Long authenticatedId = userDetails.getId();

        // User can access their own messages
        if (role.equals("ROLE_USER") && authenticatedId.equals(userId)) {
            return true;
        }

        // Worker can access messages sent to them
        if (role.equals("ROLE_WORKER") && authenticatedId.equals(workerId)) {
            return true;
        }

        return false;
    }

    public boolean isCurrentUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        HandymanUserDetails userDetails = (HandymanUserDetails) auth.getPrincipal();
        return userDetails.getRole().equals("ROLE_USER") && userDetails.getId().equals(userId);
    }

    public boolean isCurrentWorker(Long workerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        HandymanUserDetails userDetails = (HandymanUserDetails) auth.getPrincipal();
        return userDetails.getRole().equals("ROLE_WORKER") && userDetails.getId().equals(workerId);
    }

    public boolean canDeleteMessage(Long messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        HandymanUserDetails userDetails = (HandymanUserDetails) auth.getPrincipal();
        Message message = messageRepository.findById(messageId).orElse(null);
        
        if (message == null) {
            return false;
        }

        // Message can be deleted by either the sender or receiver
        if (userDetails.getRole().equals("ROLE_USER")) {
            return message.getUser().getId().equals(userDetails.getId());
        } else if (userDetails.getRole().equals("ROLE_WORKER")) {
            return message.getWorker().getId().equals(userDetails.getId());
        }

        return false;
    }
}
