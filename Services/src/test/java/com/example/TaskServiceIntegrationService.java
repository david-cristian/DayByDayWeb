package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskServiceIntegrationService extends AbstractIntegrationTest {
    @Autowired
    private TaskService taskService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User("task_owner", "pass", "Task", "Owner", "task@test.com");
        savedUser = userRepository.save(user);
    }

    @Test
    void testCreateAndRetrieveTask() {
        Task task = new Task();
        task.setTitle("Integration Task");
        task.setDescription("Testing with Testcontainers");

        Task createdTask = taskService.create(savedUser.getId(), task);

        assertNotNull(createdTask.getId());
        assertEquals("Integration Task", createdTask.getTitle());
        assertEquals(savedUser.getId(), createdTask.getOwner().getId());
    }

    @Test
    void testGetTasksByUser() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        taskService.create(savedUser.getId(), task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        taskService.create(savedUser.getId(), task2);

        List<Task> userTasks = taskService.getTasksByUser(savedUser.getId());

        assertEquals(2, userTasks.size());
    }
}
