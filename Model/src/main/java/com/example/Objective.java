package com.example;

import jakarta.persistence.Table;

import java.time.LocalDate;

@jakarta.persistence.Entity
@Table(name = "objectives")
public class Objective extends SchedulableEntity {
    public Objective() {
        super();
    }

    public Objective(String title, LocalDate dueDate, String description, User user) {
        super(title, dueDate, description, user);
    }
}
