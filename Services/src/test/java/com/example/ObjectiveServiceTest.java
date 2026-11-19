package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObjectiveServiceTest {

    @Mock
    private IObjectiveRepository objectiveRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private ObjectiveService objectiveService;

    @Test
    void createObjective_Success() {
        User owner = new User("owner", "pass", "O", "W", "o@test.com");
        owner.setId(1L);
        Objective obj = new Objective();
        obj.setTitle("Test Objective");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(objectiveRepository.save(obj)).thenReturn(obj);

        Objective createdObj = objectiveService.create(1L, obj);

        assertNotNull(createdObj);
        assertEquals(owner, createdObj.getOwner());
        verify(objectiveRepository, times(1)).save(obj);
    }
}