package com.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupService {
    private final IGroupRepository groupRepository;
    private final IUserRepository userRepository;
    private final IObjectiveRepository objectiveRepository;
    private ITaskRepository taskRepository;

    public GroupService(IGroupRepository groupRepository, IUserRepository userRepository, IObjectiveRepository objectiveRepository, ITaskRepository taskRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Group createGroup(String groupName, Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + adminId + " not found."));
        Group group = new Group(groupName, admin);
        return groupRepository.save(group);
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group with ID: " + id + " not found."));
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional
    public Group addUserToGroup(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found."));
        group.addUser(user);
        return groupRepository.save(group);
    }

    @Transactional
    public Group removeUserFromGroup(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found."));
        group.removeUser(user);
        return groupRepository.save(group);
    }

    @Transactional
    public Task addTaskToGroup(Long groupId, Task task, Long ownerId) {
        Group group = getGroupById(groupId);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + ownerId + " not found."));

        task.setOwner(owner);
        task.setGroup(group);
        group.getTasks().add(task);

        return taskRepository.save(task);
    }

    @Transactional
    public void removeTaskFromGroup(Long groupId, Long taskId) {
        Group group = getGroupById(groupId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID: " + taskId + " not found."));
        if (task.getGroup() == null || !task.getGroup().getId().equals(groupId)) {
            throw new ServiceException("Task with ID: " + taskId + " does not belong to this group.");
        }
        group.removeTask(task);
        taskRepository.delete(task);
    }

    @Transactional
    public Objective addObjectiveToGroup(Long groupId, Objective objective, Long ownerId) {
        Group group = getGroupById(groupId);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + ownerId + " not found."));

        objective.setOwner(owner);
        objective.setGroup(group);
        group.getObjectives().add(objective);

        return objectiveRepository.save(objective);
    }

    @Transactional
    public void removeObjectiveFromGroup(Long groupId, Long objectiveId) {
        Group group = getGroupById(groupId);
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ResourceNotFoundException("Objective with ID: " + objectiveId + " not found."));
        if  (objective.getGroup() == null || !objective.getGroup().getId().equals(groupId)) {
            throw new ServiceException("Objective with ID: " + objectiveId + " does not belong to this group.");
        }
        group.removeObjective(objective);
        objectiveRepository.delete(objective);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group with ID: " + groupId + " not found.");
        }
        groupRepository.deleteById(groupId);
    }
}
