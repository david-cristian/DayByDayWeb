package com.example;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService extends SchedulableService<Task, ITaskRepository> {
    public TaskService(ITaskRepository iTaskRepository, IUserRepository userRepository) {
        super(iTaskRepository, userRepository);
    }

    public List<Task> getTasksByUser(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID: " + userId + " not found."));
        return repo.findByOwner(owner);
    }
}
