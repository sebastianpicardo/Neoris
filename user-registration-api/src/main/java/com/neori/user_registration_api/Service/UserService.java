package com.neori.user_registration_api.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.neori.user_registration_api.Entity.User;
import com.neori.user_registration_api.Repository.UserRepository;
import com.neori.user_registration_api.Service.JWTService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;
    
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
        String token = jwtService.generateToken(user);
        user.setToken(token);
      //  user.setToken(UUID.randomUUID().toString());
        user.setIsActive(true);
        
        return Optional.of(userRepository.save(user));
    }
}
