package com.example.controllers;

import com.example.DTOs.GroupDtos;
import com.example.DTOs.ObjectiveDtos;
import com.example.DTOs.TaskDtos;
import com.example.Group;
import com.example.GroupService;
import com.example.Objective;
import com.example.Task;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<GroupDtos.GroupResponse> create(@Valid @RequestBody GroupDtos.CreateGroupRequest request) {
        Group created = groupService.createGroup(request.name(), request.adminId());
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDtos.GroupResponse.from(created));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<GroupDtos.GroupResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(GroupDtos.GroupResponse.from(groupService.getGroupById(id)));
    }

    @PostMapping("/{groupId}/members/{userId}")
    @Transactional
    public ResponseEntity<GroupDtos.GroupResponse> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
        Group updated = groupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok(GroupDtos.GroupResponse.from(updated));
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    @Transactional
    public ResponseEntity<GroupDtos.GroupResponse> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        Group updated = groupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok(GroupDtos.GroupResponse.from(updated));
    }

    @PostMapping("/{groupId}/tasks")
    @Transactional
    public ResponseEntity<TaskDtos.TaskResponse> addTask(@PathVariable Long groupId, @Valid @RequestBody TaskDtos.TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        if (request.status() != null)
            task.setStatus(request.status());

        Task created = groupService.addTaskToGroup(groupId, task, request.ownerId());
        return ResponseEntity.ok(TaskDtos.TaskResponse.from(created));
    }

    @DeleteMapping("/{groupId}/tasks/{taskId}")
    @Transactional
    public ResponseEntity<TaskDtos.TaskResponse> removeTask(@PathVariable Long groupId, @PathVariable Long taskId) {
        groupService.removeTaskFromGroup(groupId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/objectives")
    @Transactional
    public ResponseEntity<ObjectiveDtos.ObjectiveResponse> addObjective(@PathVariable Long groupId, @Valid @RequestBody ObjectiveDtos.ObjectiveResponse request) {
        Objective objective = new Objective();
        objective.setTitle(request.title());
        objective.setDescription(request.description());
        objective.setDueDate(request.dueDate());
        if (request.status() != null)
            objective.setStatus(request.status());

        Objective created = groupService.addObjectiveToGroup(groupId, objective, request.ownerId());
        return ResponseEntity.ok(ObjectiveDtos.ObjectiveResponse.from(created));
    }

    @DeleteMapping("/{groupId}/objectives/{objectiveId}")
    @Transactional
    public ResponseEntity<ObjectiveDtos.ObjectiveResponse> removeObjective(@PathVariable Long groupId, @PathVariable Long objectiveId) {
        groupService.removeObjectiveFromGroup(groupId, objectiveId);
        return ResponseEntity.noContent().build();
    }

}
