package com.std_data_mgmt.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.std_data_mgmt.app.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
