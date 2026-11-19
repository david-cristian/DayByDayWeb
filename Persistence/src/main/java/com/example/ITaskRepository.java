package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwner(User owner);
    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findByGroup(Group group);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Task t where t.owner.id = :userId and t.group is null")
    void deletePersonalByOwnerId(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Task t set t.owner = null where t.owner.id = :userId")
    void clearOwner(@Param("userId") Long userId);
}
