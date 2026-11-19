package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ObjectiveServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private ObjectiveService objectiveService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        objectiveRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User("obj_owner", "pass", "Obj", "Owner", "obj@test.com");
        savedUser = userRepository.save(user);
    }

    @Test
    void testCreateObjective() {
        Objective obj = new Objective();
        obj.setTitle("Integration Objective");

        Objective createdObj = objectiveService.create(savedUser.getId(), obj);

        assertNotNull(createdObj.getId());
        assertEquals("Integration Objective", createdObj.getTitle());
    }
}
