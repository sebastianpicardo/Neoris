package com.muruna.user_registration_api;






import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.neori.user_registration_api.Controller.Entity.User;
import com.neori.user_registration_api.Controller.Repository.UserRepository;
import com.neori.user_registration_api.Controller.Service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.registerUser(user);

        assertTrue(result.isPresent());
    }
}