package com.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alex_dev");
        user.setPassword("pass1234");
        user.setFirstName("Alex");
        user.setLastName("Popescu");
        user.setEmail("alex@example.com");

        List<Group> groups = new ArrayList<>();
        user.setGroups(groups);

        assertEquals(1L, user.getId());
        assertEquals("alex_dev", user.getUsername());
        assertEquals("pass1234", user.getPassword());
        assertEquals("Alex", user.getFirstName());
        assertEquals("Popescu", user.getLastName());
        assertEquals("alex@example.com", user.getEmail());
        assertEquals(groups, user.getGroups());
    }

    @Test
    public void testUserEqualsAndHashCodeBranches() {
        User u1 = new User("user1", "pass", "A", "B", "a@b.com");
        User u2 = new User("user2", "pass", "C", "D", "c@d.com");

        assertEquals(u1, u1);
        assertNotEquals(null, u1);
        assertNotEquals("String aleatoriu", u1);
        assertNotEquals(u1, u2);
        u1.setId(1L);
        assertNotEquals(u1, u2);
        assertNotEquals(u2, u1);

        u2.setId(1L);
        assertEquals(u1, u2);

        u2.setId(2L);
        assertNotEquals(u1, u2);

        assertNotNull(u1.hashCode());
        assertEquals(u1.hashCode(), u1.hashCode());
    }
}