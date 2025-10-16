package com.timex.api.service.impl;

import com.timex.api.dto.ProjectDto;
import com.timex.api.exception.ApiException;
import com.timex.api.exception.ResourceNotFoundException;
import com.timex.api.mapper.ProjectMapper;
import com.timex.api.model.Project;
import com.timex.api.model.User;
import com.timex.api.repository.ProjectRepository;
import com.timex.api.repository.UserRepository;
import com.timex.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectDto.Response createProject(String username, ProjectDto.Request request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (projectRepository.existsByNameAndUserId(request.getName(), user.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Project with this name already exists for the user");
        }

        Project project = projectMapper.toEntity(request);
        project.setUser(user);

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto.Response getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        checkProjectOwnership(project);

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto.DetailedResponse getProjectWithTasks(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        checkProjectOwnership(project);

        return projectMapper.toDetailedDto(project);
    }

    @Override
    public List<ProjectDto.Response> getAllProjects(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return projectRepository.findByUserId(user.getId()).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto.Response updateProject(Long id, ProjectDto.Request request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        checkProjectOwnership(project);

        // Check if name is being changed and if it already exists for this user
        if (!project.getName().equals(request.getName()) &&
                projectRepository.existsByNameAndUserId(request.getName(), project.getUser().getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Project with this name already exists for the user");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        checkProjectOwnership(project);

        projectRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndUsername(String name, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return projectRepository.existsByNameAndUserId(name, user.getId());
    }

    private void checkProjectOwnership(Project project) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!project.getUser().getUsername().equals(currentUsername) &&
                !hasAdminRole()) {
            throw new AccessDeniedException("You don't have permission to access this project");
        }
    }

    private boolean hasAdminRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(User.Role.ROLE_ADMIN.name()));
    }
}