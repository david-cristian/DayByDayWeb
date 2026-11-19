package com.example;

import jakarta.persistence.Table;

import java.time.LocalDate;

@jakarta.persistence.Entity
@Table(name = "tasks")
public class Task extends SchedulableEntity {
    public Task() { super(); }
    public Task(String title, LocalDate dueDate, String description, User user) {
        super(title, dueDate, description, user);
    }
}
