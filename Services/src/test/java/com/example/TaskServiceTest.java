package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_Success() {
        User owner = new User("owner", "pass", "O", "W", "o@test.com");
        owner.setId(1L);
        Task task = new Task();
        task.setTitle("Test Task");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.create(1L, task);

        assertNotNull(createdTask);
        assertEquals(owner, createdTask.getOwner());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void createTask_UserNotFound_ThrowsException() {
        Task task = new Task();
        task.setTitle("Test Task");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.create(1L, task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTasksByUser_Success() {
        User owner = new User("owner", "pass", "O", "W", "o@test.com");
        owner.setId(1L);
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(taskRepository.findByOwner(owner)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getTasksByUser(1L);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByOwner(owner);
    }
}
