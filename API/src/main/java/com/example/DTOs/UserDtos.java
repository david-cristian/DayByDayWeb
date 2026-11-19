package com.example.DTOs;

import com.example.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDtos {
    public record RegisterUserRequest(
            @NotBlank String username,
            @NotBlank @Size(min = 6) String password,
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank @Email String email
            ) {}

    public record UpdateUserRequest(
            String firstName,
            String lastName,
            @Email String email
    ) {}

    public record ChangePasswordRequest(
            @NotBlank String oldPassword,
            @NotBlank @Size(min = 6) String newPassword
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record UserResponse(
            Long id,
            String username,
            String firstName,
            String lastName,
            String email
    ) {
        public static UserResponse from(User user) {
            return new UserResponse(user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail());
        }
    }
}
