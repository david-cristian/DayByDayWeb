package com.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    @Test
    public void testGroupDefaultConstructorAndSetters() {
        Group group = new Group();
        assertNull(group.getId());

        group.setId(10L);
        group.setName("Devs");

        User admin = new User("admin", "pass", "Admin", "User", "admin@email.com");
        group.setAdmin(admin);

        List<User> users = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        List<Objective> objectives = new ArrayList<>();

        group.setUsers(users);
        group.setTasks(tasks);
        group.setObjectives(objectives);

        assertEquals(10L, group.getId());
        assertEquals("Devs", group.getName());
        assertEquals(admin, group.getAdmin());
        assertEquals(users, group.getUsers());
        assertEquals(tasks, group.getTasks());
        assertEquals(objectives, group.getObjectives());
    }

    @Test
    public void testGroupHelperMethods() {
        User admin = new User("admin", "pass", "Admin", "User", "admin@email.com");
        Group group = new Group("Dev Team", admin);

        assertTrue(group.getUsers().contains(admin));

        User member = new User("member", "pass", "Mem", "Ber", "mem@email.com");
        group.addUser(member);
        assertTrue(group.getUsers().contains(member));

        group.addUser(member);
        assertEquals(2, group.getUsers().size());

        group.removeUser(member);
        assertFalse(group.getUsers().contains(member));

        group.removeUser(member);

        Task task = new Task("Task 1", LocalDate.now(), "Desc", admin);
        group.addTask(task);
        assertTrue(group.getTasks().contains(task));
        assertEquals(group, task.getGroup());

        group.removeTask(task);
        assertFalse(group.getTasks().contains(task));
        assertNull(task.getGroup());
    }

    @Test
    public void testGroupEqualsAndHashCodeBranches() {
        User admin = new User("admin", "pass", "Admin", "User", "admin@email.com");
        Group g1 = new Group("G1", admin);
        Group g2 = new Group("G2", admin);

        assertEquals(g1, g1);
        assertNotEquals(null, g1);
        assertNotEquals("Nu e un grup", g1);
        assertNotEquals(g1, g2);
        g1.setId(1L);
        assertNotEquals(g1, g2);
        g2.setId(1L);
        assertEquals(g1, g2);
        g2.setId(2L);
        assertNotEquals(g1, g2);
        assertNotNull(g1.hashCode());
        assertEquals(g1.hashCode(), g1.hashCode());
    }
}
