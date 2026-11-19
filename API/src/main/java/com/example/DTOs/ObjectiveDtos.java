package com.example.DTOs;

import com.example.Objective;
import com.example.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ObjectiveDtos {

    public record ObjectiveRequest(
            @NotBlank String title,
            String description,
            LocalDate dueDate,
            Status status,
            @NotNull Long ownerId
    ) {}

    public record ObjectiveUpdateRequest(
            String title,
            String description,
            LocalDate dueDate,
            Status status
    ) {}
    public record ObjectiveResponse(
            Long id,
            String title,
            String description,
            LocalDate dueDate,
            Status status,
            Long ownerId
    ) {
        public static ObjectiveResponse from(Objective objective) {
            return new ObjectiveResponse(
                    objective.getId(),
                    objective.getTitle(),
                    objective.getDescription(),
                    objective.getDueDate(),
                    objective.getStatus(),
                    objective.getOwner() != null ? objective.getOwner().getId() : null
            );
        }
    }
}
