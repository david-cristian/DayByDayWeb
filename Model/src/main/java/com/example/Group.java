package com.example;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "app_groups")
public class Group extends com.example.Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Objective> objectives = new ArrayList<>();

    public Group() {}

    public Group(String name, User admin) {
        this.name = name;
        this.admin = admin;
        this.addUser(admin);
    }

    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getGroups().add(this);
        }
    }

    public void removeUser(User user) {
        if (this.users.contains(user)) {
            this.users.remove(user);
            user.getGroups().remove(this);
        }
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setGroup(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setGroup(null);
    }

    public void addObjective(Objective objective) {
        this.objectives.add(objective);
        objective.setGroup(this);
    }

    public void removeObjective(Objective objective) {
        this.objectives.remove(objective);
        objective.setGroup(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public List<Objective> getObjectives() { return objectives; }
    public void setObjectives(List<Objective> objectives) { this.objectives = objectives; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id != null && Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}