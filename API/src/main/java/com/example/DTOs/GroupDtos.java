package com.example.DTOs;
import java.util.List;

import com.example.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GroupDtos {
    public record CreateGroupRequest(
            @NotBlank String name,
            @NotNull Long adminId
    ) {}


    public record GroupResponse(
            Long id,
            String name,
            Long adminId,
            List<UserDtos.UserResponse> members,
            List<TaskDtos.TaskResponse> tasks
    ) {
        public static GroupResponse from(Group group) {
            return new GroupResponse(
                    group.getId(),
                    group.getName(),
                    group.getAdmin() != null ? group.getAdmin().getId() : null,
                    group.getUsers().stream().map(UserDtos.UserResponse::from).toList(),
                    group.getTasks().stream().map(TaskDtos.TaskResponse::from).toList()
            );
        }
    }
}
