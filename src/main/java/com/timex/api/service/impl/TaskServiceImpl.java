package com.timex.api.service.impl;

import com.timex.api.dto.TaskDto;
import com.timex.api.exception.ResourceNotFoundException;
import com.timex.api.mapper.TaskMapper;
import com.timex.api.model.Project;
import com.timex.api.model.Task;
import com.timex.api.repository.ProjectRepository;
import com.timex.api.repository.TaskRepository;
import com.timex.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDto.Response createTask(TaskDto.Request request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

        checkProjectOwnership(project);

        Task task = taskMapper.toEntity(request);
        task.setProject(project);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto.Response getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        checkProjectOwnership(task.getProject());

        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto.Response> getTasksByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        checkProjectOwnership(project);

        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto.Response> getTasksByProjectIdAndStatus(Long projectId, Task.Status status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        checkProjectOwnership(project);

        return taskRepository.findByProjectIdAndStatus(projectId, status).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto.Response> getTasksDueBeforeDate(LocalDateTime dateTime) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return taskRepository.findByDueDateBefore(dateTime).stream()
                .filter(task -> task.getProject().getUser().getUsername().equals(currentUsername) || hasAdminRole())
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto.Response updateTask(Long id, TaskDto.Request request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        checkProjectOwnership(task.getProject());

        // Check if project is being changed
        if (!task.getProject().getId().equals(request.getProjectId())) {
            Project newProject = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", request.getProjectId()));

            // Check if user has access to the new project
            checkProjectOwnership(newProject);

            task.setProject(newProject);
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setActualHours(request.getActualHours());
        task.setDueDate(request.getDueDate());

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto.Response updateTaskStatus(Long id, Task.Status status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        checkProjectOwnership(task.getProject());

        task.setStatus(status);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        checkProjectOwnership(task.getProject());

        taskRepository.deleteById(id);
    }

    private void checkProjectOwnership(Project project) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!project.getUser().getUsername().equals(currentUsername) && !hasAdminRole()) {
            throw new AccessDeniedException("You don't have permission to access this project");
        }
    }

    private boolean hasAdminRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}