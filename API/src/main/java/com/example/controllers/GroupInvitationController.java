package com.example.controllers;

import com.example.DTOs.GroupInvitationDtos;
import com.example.GroupInvitation;
import com.example.GroupInvitationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupInvitationController {
    private final GroupInvitationService groupInvitationService;
    public GroupInvitationController(GroupInvitationService groupInvitationService) {
        this.groupInvitationService = groupInvitationService;
    }

    @PostMapping("/groups/{groupId}/invites")
    @Transactional
    public ResponseEntity<GroupInvitationDtos.GroupInvitationResponse> sendInvite(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupInvitationDtos.SendInviteRequest request) {
        GroupInvitation invitation = groupInvitationService.sendInvite(
                groupId,
                request.inviterId(),
                request.inviteeId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GroupInvitationDtos.GroupInvitationResponse.from(invitation));
    }

    @GetMapping("/users/{userId}/invites")
    @Transactional(readOnly = true)
    public ResponseEntity<List<GroupInvitationDtos.GroupInvitationResponse>> getUserPendingInvites(@PathVariable Long userId) {
        List<GroupInvitationDtos.GroupInvitationResponse> invites = groupInvitationService.getPendingInvitesForUser(userId)
                .stream()
                .map(GroupInvitationDtos.GroupInvitationResponse::from)
                .toList();
        return ResponseEntity.ok(invites);
    }

    @PostMapping("/invites/{inviteId}/accept")
    public ResponseEntity<Void> acceptInvite(
            @PathVariable Long inviteId,
            @RequestParam Long userId) {
        groupInvitationService.acceptInvite(inviteId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/invites/{inviteId}/decline")
    public ResponseEntity<Void> declineInvite(
            @PathVariable Long inviteId,
            @RequestParam Long userId) {
        groupInvitationService.declineInvite(inviteId, userId);
        return ResponseEntity.noContent().build();
    }
}
