package com.example;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ObjectiveService extends SchedulableService<Objective, IObjectiveRepository> {
    public ObjectiveService(IObjectiveRepository iObjectiveRepository, IUserRepository userRepository) {
        super(iObjectiveRepository, userRepository);
    }

    public List<Objective> getObjectivesByUser(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User ID: " + userId + " not found."));
        return repo.findByOwner(owner);
    }
}
