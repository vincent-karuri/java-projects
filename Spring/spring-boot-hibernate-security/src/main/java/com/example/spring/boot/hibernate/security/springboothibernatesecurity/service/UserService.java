package com.example.spring.boot.hibernate.security.springboothibernatesecurity.service;

import com.example.spring.boot.hibernate.security.springboothibernatesecurity.domain.User;
import com.example.spring.boot.hibernate.security.springboothibernatesecurity.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByEmail(String email);
    User save(UserRegistrationDto userRegistrationDto);
}
