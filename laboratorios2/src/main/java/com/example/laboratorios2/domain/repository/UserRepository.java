package com.example.laboratorios2.domain.repository;

import java.util.Optional;

import com.example.laboratorios2.domain.model.User;

public interface UserRepository {
    Optional<User> findById(int id);
    Optional<User> findAdminByArea(String area);
}
