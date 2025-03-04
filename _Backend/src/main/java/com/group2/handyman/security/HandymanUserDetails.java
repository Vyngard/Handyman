package com.group2.handyman.security;

import com.group2.handyman.model.User;
import com.group2.handyman.model.Worker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class HandymanUserDetails implements UserDetails {
    private String username;
    private String password;
    private String role;
    private Long id;

    public HandymanUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = "ROLE_USER";
        this.id = user.getId();
    }

    public HandymanUserDetails(Worker worker) {
        this.username = worker.getUsername();
        this.password = worker.getPassword();
        this.role = "ROLE_WORKER";
        this.id = worker.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
