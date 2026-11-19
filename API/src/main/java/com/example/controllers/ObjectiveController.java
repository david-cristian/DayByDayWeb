package com.example.controllers;

import java.util.List;
import com.example.DTOs.ObjectiveDtos;
import com.example.DTOs.TaskDtos;
import com.example.Objective;
import com.example.ObjectiveService;
import com.example.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/objectives")
public class ObjectiveController {

    private final ObjectiveService objectiveService;

    public ObjectiveController(ObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    @PostMapping
    public ResponseEntity<ObjectiveDtos.ObjectiveResponse> create(@Valid @RequestBody ObjectiveDtos.ObjectiveRequest request) {
        Objective objective = new Objective();
        objective.setTitle(request.title());
        objective.setDescription(request.description());
        objective.setDueDate(request.dueDate());
        if (request.status() != null) {
            objective.setStatus(request.status());
        }

        Objective created = objectiveService.create(request.ownerId(), objective);
        return ResponseEntity.status(HttpStatus.CREATED).body(ObjectiveDtos.ObjectiveResponse.from(created));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ObjectiveDtos.ObjectiveResponse>> getByUser(@PathVariable Long userId) {
        List<ObjectiveDtos.ObjectiveResponse> objectives = objectiveService.getObjectivesByUser(userId).stream()
                .map(ObjectiveDtos.ObjectiveResponse::from)
                .toList();
        return ResponseEntity.ok(objectives);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        objectiveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
