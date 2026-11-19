package com.example.controllers;
import java.util.List;
import com.example.DTOs.UserDtos;
import com.example.User;
import com.example.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtos.UserResponse> register(@Valid @RequestBody UserDtos.RegisterUserRequest request) {
        User user = new User(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.email()
        );
        User saved = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtos.UserResponse.from(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDtos.UserResponse> login(@Valid @RequestBody UserDtos.LoginRequest request) {
        User authenticated = userService.login(request.username(), request.password());
        return ResponseEntity.ok(UserDtos.UserResponse.from(authenticated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtos.UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserDtos.UserResponse.from(userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtos.UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserDtos.UpdateUserRequest request) {
        User updatedDetails = new User();
        updatedDetails.setFirstName(request.firstName());
        updatedDetails.setLastName(request.lastName());
        updatedDetails.setEmail(request.email());

        User updated = userService.updateUser(id, updatedDetails);
        return ResponseEntity.ok(UserDtos.UserResponse.from(updated));
    }

    @GetMapping
    public ResponseEntity<List<UserDtos.UserResponse>> getAll() {
        List<UserDtos.UserResponse> users = userService.getAllUsers().stream()
                .map(UserDtos.UserResponse::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
