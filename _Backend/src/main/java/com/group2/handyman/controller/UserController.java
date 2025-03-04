package com.group2.handyman.controller;

import com.group2.handyman.dto.request.UserCreateDto;
import com.group2.handyman.dto.request.UserUpdateDto;
import com.group2.handyman.model.User;
import com.group2.handyman.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "${handyman.cors.allowed-origins}")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto userDto) {
        User updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}/credit")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> updateCredit(
            @PathVariable Long id,
            @RequestParam double amount) {
        userService.updateCredit(id, amount);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getUserRating(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user.getAverageRating());
    }

    @GetMapping("/{id}/jobs/completed")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<List<User>> getCompletedJobs(@PathVariable Long id) {
        List<User> users = userService.searchUsers("completed");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/jobs/pending")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<List<User>> getPendingJobs(@PathVariable Long id) {
        List<User> users = userService.searchUsers("pending");
        return ResponseEntity.ok(users);
    }
}
