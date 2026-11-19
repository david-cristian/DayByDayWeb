package com.example;


import jakarta.persistence.*;

import java.time.LocalDate;

@MappedSuperclass
public abstract class SchedulableEntity extends Entity<Long> {
    @Column(nullable = false)
    protected String title;

    protected LocalDate dueDate;

    protected String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Status status = Status.IN_PROGRESS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    protected User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    protected Group group;

    public SchedulableEntity() {
        super();
    }

    public SchedulableEntity(String title, LocalDate dueDate, String description, User owner) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
