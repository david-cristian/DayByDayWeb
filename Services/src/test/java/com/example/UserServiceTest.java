package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_Success() {
        User newUser = new User("alex", "pass123", "Alex", "Popescu", "alexpopescu@gmail.com");
        when(userRepository.findByUsername("alex")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("parola_criptata_123");
        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.registerUser(newUser);

        assertNotNull(savedUser);
        assertEquals("alex", savedUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
        verify(passwordEncoder, times(1)).encode("pass123");
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void registerUser_ThrowsExceptionWhenUsernameExists() {
        User existingUser = new User("alex", "pass123", "Alex", "Popescu", "alexpopescu@gmail.com");
        when(userRepository.findByUsername("alex")).thenReturn(Optional.of(existingUser));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.registerUser(existingUser);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
