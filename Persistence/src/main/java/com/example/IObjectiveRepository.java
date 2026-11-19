package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IObjectiveRepository extends JpaRepository<Objective, Long> {
    List<Objective> findByOwner(User owner);
    List<Objective> findByGroup(Group group);
    List<Objective> findByDueDate(LocalDate dueDate);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Objective o where o.owner.id = :userId and o.group is null")
    void deletePersonalByOwnerId(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Objective o set o.owner = null where o.owner.id = :userId")
    void clearOwner(@Param("userId") Long userId);
}
