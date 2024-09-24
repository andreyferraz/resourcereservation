package com.example.resourcereservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resourcereservation.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
