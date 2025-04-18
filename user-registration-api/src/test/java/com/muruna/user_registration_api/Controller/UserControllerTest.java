package com.muruna.user_registration_api.Controller;

import com.neori.user_registration_api.Controller.Controller.UserController;
import com.neori.user_registration_api.Controller.Entity.User;
import com.neori.user_registration_api.Controller.Repository.UserRepository;
import com.neori.user_registration_api.Controller.Service.JWTService;
import com.neori.user_registration_api.DTO.UserRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_ValidUser_ReturnsCreated() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Sebastián Picardo");
        userRequestDTO.setEmail("seba@ejemplo.cl");
        userRequestDTO.setPassword("seba123");
        
        
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/createUser")
                .contentType("application/json")
                .content("{\"name\":\"Sebastián Picardo\", \"email\":\"seba@ejemplo.cl\", \"password\":\"seba123\", \"phones\": []}"))
                .andExpect(status().isOk());
        
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void createUser_EmailAlreadyExists_ReturnsConflict() throws Exception {
      
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("seba@ejemplo.cl");
        when(userRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(new User()));

    
        mockMvc.perform(post("/api/users/createUser")
                .contentType("application/json")
                .content("{\"name\":\"Sebastián Picardo\", \"email\":\"seba@ejemplo.cl\", \"password\":\"seba123\", \"phones\": []}"))
                .andExpect(status().isConflict());
    }
}