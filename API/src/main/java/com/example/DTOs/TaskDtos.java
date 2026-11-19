package com.example.DTOs;

import com.example.Status;
import com.example.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDtos {
    public record TaskRequest(
            @NotBlank String title,
            String description,
            LocalDate dueDate,
            Status status,
            @NotNull Long ownerId
    ) {}

    public record UpdateTaskRequest(
            String title,
            String description,
            LocalDate dueDate,
            Status status
    ) {}

    public record TaskResponse(
            Long id,
            String title,
            String description,
            LocalDate dueDate,
            Status status,
            Long ownerId,
            Long groupId
    ) {
        public static TaskResponse from(Task task) {
            return new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getStatus(),
                    task.getOwner() != null ? task.getOwner().getId() : null,
                    task.getGroup() != null ? task.getGroup().getId() : null
            );
        }
    }
}
