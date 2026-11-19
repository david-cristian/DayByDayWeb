package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IGroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {
    boolean existsByGroupIdAndInviteeIdAndStatus(Long groupId, Long inviteeId, InvitationStatus status);
    List<GroupInvitation> findByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);
}
