package com.timex.api.controller;

import com.timex.api.dto.ProjectDto;
import com.timex.api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "Operations related to project management")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new project", description = "Creates a new project for the authenticated user")
    public ResponseEntity<ProjectDto.Response> createProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProjectDto.Request request) {
        return new ResponseEntity<>(
                projectService.createProject(userDetails.getUsername(), request),
                HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all user projects", description = "Returns all projects for the authenticated user")
    public ResponseEntity<List<ProjectDto.Response>> getAllProjects(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getAllProjects(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get project by ID", description = "Returns a specific project by ID")
    public ResponseEntity<ProjectDto.Response> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/{id}/detailed")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get project with tasks", description = "Returns a project with all its tasks")
    public ResponseEntity<ProjectDto.DetailedResponse> getProjectWithTasks(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectWithTasks(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update project", description = "Updates an existing project")
    public ResponseEntity<ProjectDto.Response> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDto.Request request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete project", description = "Deletes a project")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Check project name availability", description = "Checks if a project name is already taken by the user")
    public ResponseEntity<Boolean> checkProjectNameAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String name) {
        return ResponseEntity.ok(projectService.existsByNameAndUsername(name, userDetails.getUsername()));
    }
}