package com.neori.user_registration_api.Controller.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.neori.user_registration_api.Controller.Entity.User;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
