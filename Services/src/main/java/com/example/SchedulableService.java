package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class SchedulableService<T extends SchedulableEntity, Repo extends JpaRepository<T, Long>> {
    protected final Repo repo;
    protected final IUserRepository userRepository;

    public SchedulableService(Repo repo, IUserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    @Transactional
    public T create(Long userId, T entity) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found."));
        entity.setOwner(owner);
        return repo.save(entity);
    }

    public T getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + id + " not found."));
    }

    @Transactional
    public T updateStatus(Long id, Status status) {
        T entity = getById(id);
        entity.setStatus(status);
        return repo.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User with ID: " + id + " not found.");
        }
        repo.deleteById(id);
    }
}
