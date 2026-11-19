package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected IObjectiveRepository objectiveRepository;

    @Autowired
    protected ITaskRepository taskRepository;

    @Autowired
    protected IGroupRepository groupRepository;

    @Autowired
    protected IUserRepository userRepository;

    @AfterEach
    void cleanUpDatabase() {
        em.clear();
        objectiveRepository.deleteAllInBatch();
        taskRepository.deleteAllInBatch();

        groupRepository.deleteAll();

        userRepository.deleteAllInBatch();
    }
}
