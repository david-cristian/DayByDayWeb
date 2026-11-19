package com.example.controllers;

import java.util.List;
import com.example.DTOs.TaskDtos;
import com.example.Task;
import com.example.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDtos.TaskResponse> create(@Valid @RequestBody TaskDtos.TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setDueDate(taskRequest.dueDate());
        if (taskRequest.status() != null) {
            task.setStatus(taskRequest.status());
        }

        Task created = taskService.create(taskRequest.ownerId(), task);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskDtos.TaskResponse.from(created));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDtos.TaskResponse>> getByUser(@PathVariable Long userId) {
        List<TaskDtos.TaskResponse> tasks = taskService.getTasksByUser(userId).stream()
                .map(TaskDtos.TaskResponse::from)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDtos.TaskResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(TaskDtos.TaskResponse.from(taskService.getById(id)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
