package com.group2.handyman.service;

import com.group2.handyman.dto.request.UserCreateDto;
import com.group2.handyman.dto.request.UserUpdateDto;
import com.group2.handyman.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(UserCreateDto userDto);
    User updateUser(Long id, UserUpdateDto userDto);
    void deleteUser(Long id);
    User findByEmailOrUsername(String identifier);
    boolean validatePassword(User user, String password);
    void updateCredit(Long userId, double amount);
    void updateRating(Long userId, double rating);
    List<User> searchUsers(String keyword);
}
