package com.neori.user_registration_api.Controller.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.neori.user_registration_api.Controller.Entity.Phone;
import com.neori.user_registration_api.Controller.Entity.User;
import com.neori.user_registration_api.Controller.Repository.UserRepository;
import com.neori.user_registration_api.Controller.Service.UserService;
import com.neori.user_registration_api.DTO.UserRequestDTO;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")


public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
            return new ResponseEntity<>("El correo ya existe " , HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
    	
        try {
        	
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            user.setCreated(LocalDateTime.now());
            user.setModified(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());
            user.setToken(UUID.randomUUID().toString());
            user.setIsActive(true);

            List<Phone> phones = userRequestDTO.getPhones().stream()
                    .map(dto -> {
                        Phone phone = new Phone();
                        phone.setNumber(dto.getNumber());
                        phone.setCitycode(dto.getCitycode());
                        phone.setContrycode(dto.getContrycode());
                        return phone;
                    })
                    .toList();

            user.setPhones(phones);
            userRepository.save(user);

            return ResponseEntity.ok(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("El correo ya existe: " + e.getMessage());
        }
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return new ResponseEntity<>("Errores de validación: " + errorMessage, HttpStatus.BAD_REQUEST);
    }
}
