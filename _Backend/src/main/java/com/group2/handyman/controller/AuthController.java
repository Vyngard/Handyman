package com.group2.handyman.controller;

import com.group2.handyman.dto.request.UserCreateDto;
import com.group2.handyman.dto.request.WorkerCreateDto;
import com.group2.handyman.exception.HandymanException;
import com.group2.handyman.model.User;
import com.group2.handyman.model.Worker;
import com.group2.handyman.security.HandymanUserDetails;
import com.group2.handyman.security.JwtUtil;
import com.group2.handyman.service.UserService;
import com.group2.handyman.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "${handyman.cors.allowed-origins}")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> credentials) {
        try {
            String identifier = credentials.get("email"); // Can be email or username
            String password = credentials.get("password");

            if (identifier == null || password == null) {
                throw new HandymanException("INVALID_CREDENTIALS", "Email/username and password are required");
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, password)
            );

            HandymanUserDetails userDetails = (HandymanUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails, userDetails.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", userDetails.getRole());
            response.put("id", userDetails.getId());
            response.put("username", userDetails.getUsername());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "INVALID_CREDENTIALS");
            errorResponse.put("message", "Invalid email/username or password");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (HandymanException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getCode());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDto userDto) {
        try {
            User user = userService.createUser(userDto);
            HandymanUserDetails userDetails = new HandymanUserDetails(user);
            String token = jwtUtil.generateToken(userDetails, "ROLE_USER");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "ROLE_USER");
            response.put("id", user.getId());
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);
        } catch (HandymanException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getCode());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register/worker")
    public ResponseEntity<?> registerWorker(@Valid @RequestBody WorkerCreateDto workerDto) {
        try {
            Worker worker = workerService.createWorker(workerDto);
            HandymanUserDetails userDetails = new HandymanUserDetails(worker);
            String token = jwtUtil.generateToken(userDetails, "ROLE_WORKER");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "ROLE_WORKER");
            response.put("id", worker.getId());
            response.put("username", worker.getUsername());

            return ResponseEntity.ok(response);
        } catch (HandymanException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getCode());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String username = jwtUtil.extractUsername(token);
            if (username == null) {
                throw new HandymanException("INVALID_TOKEN", "Token is invalid or expired");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", username);
            return ResponseEntity.ok(response);
        } catch (HandymanException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getCode());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
