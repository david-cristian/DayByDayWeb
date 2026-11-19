package com.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ObjectiveTest {

    @Test
    public void testObjective() {
        User owner = new User("john_doe", "pass123", "John", "Doe", "john@example.com");
        Objective obj = new Objective("100k", LocalDate.of(2026, 1, 1), "bani", owner);

        assertNull(obj.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals("100k", obj.getTitle());
        assertEquals(LocalDate.of(2026, 1, 1), obj.getDueDate());
        assertEquals("bani", obj.getDescription());
        assertEquals(Status.IN_PROGRESS, obj.getStatus());
        assertEquals(owner, obj.getOwner());

        Objective obj1 = new Objective();
        obj1.setId(obj.getId());
        obj1.setTitle(obj.getTitle());
        obj1.setDueDate(obj.getDueDate());
        obj1.setDescription(obj.getDescription());
        obj1.setStatus(obj.getStatus());
        obj1.setOwner(obj.getOwner());

        assertNull(obj1.getId(), "ID-ul trebuie să fie null înainte de salvarea în DB");
        assertEquals(obj.getTitle(), obj1.getTitle());
        assertEquals(obj.getDueDate(), obj1.getDueDate());
        assertEquals(obj.getDescription(), obj1.getDescription());
        assertEquals(obj.getStatus(), obj1.getStatus());
        assertEquals(obj.getOwner(), obj1.getOwner());
    }
}