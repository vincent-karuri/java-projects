package com.example.spring.boot.hibernate.security.springboothibernatesecurity.repository;

import com.example.spring.boot.hibernate.security.springboothibernatesecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}