package com.group2.handyman.service.impl;

import com.group2.handyman.dto.request.UserCreateDto;
import com.group2.handyman.dto.request.UserUpdateDto;
import com.group2.handyman.exception.HandymanException;
import com.group2.handyman.exception.ResourceNotFoundException;
import com.group2.handyman.model.User;
import com.group2.handyman.model.UserRepository;
import com.group2.handyman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }

    @Override
    public User createUser(UserCreateDto userDto) {
        if (userRepository.findByEmailOrUsername(userDto.getEmail(), userDto.getUsername()) != null) {
            throw new HandymanException("DUPLICATE_USER", "User with this email or username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setLocation(userDto.getLocation());
        user.setCredit(0.0);
        user.setTotalJobs(0);
        user.setAverageRating(0.0);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserUpdateDto userDto) {
        User user = getUserById(id);

        User existingUser = userRepository.findByEmailOrUsername(userDto.getEmail(), userDto.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new HandymanException("DUPLICATE_USER", "Email or username already in use");
        }

        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setLocation(userDto.getLocation());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id.toString());
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmailOrUsername(String identifier) {
        return userRepository.findByEmailOrUsername(identifier, identifier);
    }

    @Override
    public boolean validatePassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    @Transactional
    public void updateCredit(Long userId, double amount) {
        User user = getUserById(userId);
        user.setCredit(user.getCredit() + amount);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateRating(Long userId, double rating) {
        User user = getUserById(userId);
        double currentRating = user.getAverageRating();
        int totalJobs = user.getTotalJobs();
        
        double newRating = ((currentRating * totalJobs) + rating) / (totalJobs + 1);
        user.setAverageRating(newRating);
        user.setTotalJobs(totalJobs + 1);
        
        userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.findByKeyword(keyword);
    }
}
