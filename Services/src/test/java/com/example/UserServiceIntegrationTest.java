package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterAndRetrieveUserFromDatabase() {
        User user = new User("docker_user", "parola", "Docker", "Test", "docker@test.com");

        userService.registerUser(user);
        User retrievedUser = userService.getUserByUsername("docker_user");

        assertNotNull(retrievedUser.getId());
        assertEquals("docker@test.com", retrievedUser.getEmail());
    }

    @Test
    void testRegisterDuplicateUsernameThrowsException() {
        User user1 = new User("duplicate_user", "parola", "D", "U", "d1@test.com");
        User user2 = new User("duplicate_user", "altaparola", "A", "U", "d2@test.com");

        userService.registerUser(user1);

        ServiceException exception = assertThrows(ServiceException.class, () -> userService.registerUser(user2));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testUpdateUserUpdatesDataCorrectly() {
        User user = new User("update_user", "pass", "Old", "Name", "old@test.com");
        User savedUser = userService.registerUser(user);

        User updatedDetails = new User();
        updatedDetails.setFirstName("New");
        updatedDetails.setLastName("Name2");
        updatedDetails.setEmail("new@test.com");

        User result = userService.updateUser(savedUser.getId(), updatedDetails);

        assertEquals("New", result.getFirstName());
        assertEquals("new@test.com", result.getEmail());
    }

    @Test
    void testDeleteUserRemovesRecordFromDatabase() {
        User user = new User("delete_user", "pass", "A", "B", "del@test.com");
        User savedUser = userService.registerUser(user);

        userService.deleteUser(savedUser.getId());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }
}
