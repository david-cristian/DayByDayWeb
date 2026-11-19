package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByNameContainingIgnoreCase(String name);

    List<Group> findByAdminId(Long id);
    List<Group> findByUsersContaining(User user);
}
