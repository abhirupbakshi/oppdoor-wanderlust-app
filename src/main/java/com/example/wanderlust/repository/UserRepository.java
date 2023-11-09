package com.example.wanderlust.repository;

import com.example.wanderlust.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
