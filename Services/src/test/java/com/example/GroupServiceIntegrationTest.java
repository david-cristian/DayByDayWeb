package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

public class GroupServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private GroupService groupService;

    private User admin;
    private User regularUser;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();

        admin = userRepository.save(new User("admin", "pass", "A", "A", "a@test.com"));
        regularUser = userRepository.save(new User("user", "pass", "U", "U", "u@test.com"));
    }

    @Test
    void testCreateGroup() {
        Group group = groupService.createGroup("New Group", admin.getId());

        assertNotNull(group.getId());
        assertEquals("New Group", group.getName());
        assertEquals(admin.getId(), group.getAdmin().getId());
    }

    @Test
    @Transactional
        // Necesar pentru a accesa colecțiile Lazy Loaded în timpul assert-urilor
    void testAddUserToGroup() {
        Group group = groupService.createGroup("Dev Team", admin.getId());

        Group updatedGroup = groupService.addUserToGroup(group.getId(), regularUser.getId());

        assertTrue(updatedGroup.getUsers().stream().anyMatch(u -> u.getId().equals(regularUser.getId())));
    }

    @Test
    @Transactional
    void testAddTaskToGroup() {
        Group group = groupService.createGroup("Dev Team", admin.getId());
        Task task = new Task();
        task.setTitle("Group Task");
        task.setOwner(admin);
        task.setStatus(Status.IN_PROGRESS);

        group.addTask(task);
        task = taskRepository.save(task);

        Task updatedTask = groupService.addTaskToGroup(group.getId(), task.getId());

        assertEquals(group.getId(), updatedTask.getGroup().getId());

        // Verificăm și din perspectiva grupului
        Group fetchedGroup = groupService.getGroupById(group.getId());
        long id = task.getId();
        assertTrue(fetchedGroup.getTasks().stream().anyMatch(t -> t.getId().equals(id)));
    }
}
