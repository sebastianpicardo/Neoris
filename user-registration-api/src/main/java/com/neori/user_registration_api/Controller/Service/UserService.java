package com.neori.user_registration_api.Controller.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.neori.user_registration_api.Controller.Entity.User;
import com.neori.user_registration_api.Controller.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.empty();
        }

        
        user.setId(UUID.randomUUID()); 
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setIsActive(true);
        
        return Optional.of(userRepository.save(user));
    }
}
