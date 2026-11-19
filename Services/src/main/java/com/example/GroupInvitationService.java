package com.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupInvitationService {

    private final IGroupInvitationRepository invitationRepository;
    private final IGroupRepository groupRepository;
    private final IUserRepository userRepository;

    public GroupInvitationService(IGroupInvitationRepository invitationRepository,
                                  IGroupRepository groupRepository,
                                  IUserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public GroupInvitation sendInvite(Long groupId, Long inviterId, Long inviteeId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new ResourceNotFoundException("Inviter not found"));
        User invitee = userRepository.findById(inviteeId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitee not found"));

        if (!group.getAdmin().getId().equals(inviterId)) {
            throw new ServiceException("Only the group admin can send invitations.");
        }
        if (group.getUsers().contains(invitee)) {
            throw new ServiceException("User is already a member of this group.");
        }
        if (invitationRepository.existsByGroupIdAndInviteeIdAndStatus(groupId, inviteeId, InvitationStatus.PENDING)) {
            throw new ServiceException("A pending invitation already exists for this user.");
        }

        GroupInvitation invitation = new GroupInvitation();
        invitation.setGroup(group);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);
        invitation.setStatus(InvitationStatus.PENDING);

        return invitationRepository.save(invitation);
    }

    public void acceptInvite(Long inviteId, Long currentUserId) {
        GroupInvitation invitation = invitationRepository.findById(inviteId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getInvitee().getId().equals(currentUserId)) {
            throw new ServiceException("You cannot accept an invitation addressed to another user.");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ServiceException("This invitation has already been processed.");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.getGroup().addUser(invitation.getInvitee());
    }

    public void declineInvite(Long inviteId, Long currentUserId) {
        GroupInvitation invitation = invitationRepository.findById(inviteId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getInvitee().getId().equals(currentUserId)) {
            throw new ServiceException("You cannot decline an invitation addressed to another user.");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ServiceException("This invitation has already been processed.");
        }

        invitation.setStatus(InvitationStatus.DECLINED);
    }

    @Transactional(readOnly = true)
    public List<GroupInvitation> getPendingInvitesForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID: " + userId + " not found.");
        }
        return invitationRepository.findByInviteeIdAndStatus(userId, InvitationStatus.PENDING);
    }
}