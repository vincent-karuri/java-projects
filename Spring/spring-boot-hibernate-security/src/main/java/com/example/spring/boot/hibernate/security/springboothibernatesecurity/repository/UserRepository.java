package com.example.spring.boot.hibernate.security.springboothibernatesecurity.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByEmail(String email);
}