package com.group2.handyman.security;

import com.group2.handyman.model.User;
import com.group2.handyman.model.Worker;
import com.group2.handyman.model.UserRepository;
import com.group2.handyman.model.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HandymanUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find a regular user
        User user = userRepository.findByEmailOrUsername(username, username);
        if (user != null) {
            return new HandymanUserDetails(user);
        }

        // If not found, try to find a worker
        Worker worker = workerRepository.findByEmailOrUsername(username, username);
        if (worker != null) {
            return new HandymanUserDetails(worker);
        }

        throw new UsernameNotFoundException("User not found with username/email: " + username);
    }
}
