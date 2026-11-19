package com.example;

import jakarta.persistence.Table;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final IUserRepository userRepository;
    private final IGroupRepository groupRepository;
    private final ITaskRepository taskRepository;
    private final IObjectiveRepository objectiveRepository;

    private final PasswordEncoder passwordEncoder;


    public UserService(IUserRepository userRepository, IGroupRepository groupRepository, ITaskRepository taskRepository, IObjectiveRepository objectiveRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.objectiveRepository = objectiveRepository;
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ServiceException("Username already exists");
        }

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Username not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("Invalid data.");
        }

        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + id + " not found."));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found."));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, User updatedDetails) {
        User existingUser = getUserById(id);
        existingUser.setFirstName(updatedDetails.getFirstName());
        existingUser.setLastName(updatedDetails.getLastName());
        existingUser.setEmail(updatedDetails.getEmail());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + id + " not found."));

        for (Group group : groupRepository.findByAdminId(id)) {
            group.removeUser(user);

            if (group.getUsers().isEmpty()) {
                groupRepository.delete(group);
            } else {
                group.setAdmin(group.getUsers().getFirst());
            }
        }

        for (Group group : new ArrayList<>(user.getGroups())) {
            group.removeUser(user);
        }

        taskRepository.deletePersonalByOwnerId(id);
        taskRepository.clearOwner(id);
        objectiveRepository.deletePersonalByOwnerId(id);
        objectiveRepository.clearOwner(id);

        userRepository.deleteById(id);
    }
}
