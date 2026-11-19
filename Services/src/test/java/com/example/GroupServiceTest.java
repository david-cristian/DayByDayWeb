package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @Mock
    private IGroupRepository groupRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ITaskRepository taskRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void addUserToGroup_Success() {
        Group group = new Group("Test Group", new User());
        group.setId(1L);
        User user = new User("user", "pass", "U", "S", "u@test.com");
        user.setId(2L);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(groupRepository.save(group)).thenReturn(group);

        Group updatedGroup = groupService.addUserToGroup(1L, 2L);

        assertTrue(updatedGroup.getUsers().contains(user));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void removeTaskFromGroup_Success() {
        Group group = new Group("Test Group", new User());
        group.setId(1L);
        Task task = new Task();
        task.setId(2L);

        group.addTask(task);
        task.setGroup(group);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = groupService.removeTaskFromGroup(1L, 2L);

        assertNull(updatedTask.getGroup());
        assertFalse(group.getTasks().contains(task));
        verify(taskRepository, times(1)).save(task);
    }
}
