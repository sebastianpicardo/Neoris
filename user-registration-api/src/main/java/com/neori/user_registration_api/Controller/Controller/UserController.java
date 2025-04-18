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
import com.neori.user_registration_api.Controller.Service.JWTService;
import com.neori.user_registration_api.Controller.Service.UserService;
import com.neori.user_registration_api.DTO.JwtResponse;
import com.neori.user_registration_api.DTO.LoginRequestDTO;
import com.neori.user_registration_api.DTO.UserRequestDTO;
import com.neori.user_registration_api.Exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Operaciones relacionadas al manejo de usuarios")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTService jwtService;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
            return new ResponseEntity<>("El correo ya existe " , HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/createUser")
    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Registra un nuevo usuario con su email, contraseña, nombre y teléfonos"
    )
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
    	
    
        try {
        	   if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
                   return new ResponseEntity<>(new ErrorResponse("El correo ya está registrado"), HttpStatus.CONFLICT);
               }
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
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Hubo un error al crear el usuario: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // Buscar al usuario por el correo electrónico
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verificar la contraseña
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Generar el token
                String token = jwtService.generateToken(user);

                // Retornar el token y el nombre de usuario en la respuesta
                JwtResponse jwtResponse = new JwtResponse(token, user.getName());
                return ResponseEntity.ok(jwtResponse);  // Respuesta con el token generado
            } else {
                return new ResponseEntity<>(new ErrorResponse("Credenciales inválidas"), HttpStatus.UNAUTHORIZED); // Credenciales incorrectas
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);  // Usuario no encontrado
        }
    }

}
