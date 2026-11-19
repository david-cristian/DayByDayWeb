package com.example.DTOs;

import com.example.GroupInvitation;
import com.example.InvitationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class GroupInvitationDtos {
    public record SendInviteRequest(
            @NotNull(message = "Inviter ID is required")
            Long inviterId,

            @NotNull(message = "Invitee ID is required")
            Long inviteeId
    ) {
    }

    public record GroupInvitationResponse(
            Long id,
            Long groupId,
            String groupName,
            Long inviterId,
            String inviterUsername,
            Long inviteeId,
            String inviteeUsername,
            InvitationStatus status,
            LocalDateTime createdAt
    ) {
        public static GroupInvitationResponse from(GroupInvitation invitation) {
            return new GroupInvitationResponse(
                    invitation.getId(),
                    invitation.getGroup().getId(),
                    invitation.getGroup().getName(),
                    invitation.getInviter().getId(),
                    invitation.getInviter().getUsername(),
                    invitation.getInvitee().getId(),
                    invitation.getInvitee().getUsername(),
                    invitation.getStatus(),
                    invitation.getCreatedAt()
            );
        }
    }
}
