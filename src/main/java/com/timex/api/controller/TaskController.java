package com.timex.api.controller;

import com.timex.api.dto.TaskDto;
import com.timex.api.model.Task;
import com.timex.api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Operations related to task management")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new task", description = "Creates a new task for a specified project")
    public ResponseEntity<TaskDto.Response> createTask(@Valid @RequestBody TaskDto.Request request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get task by ID", description = "Returns a specific task by ID")
    public ResponseEntity<TaskDto.Response> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all tasks for a project", description = "Returns all tasks for a specific project")
    public ResponseEntity<List<TaskDto.Response>> getTasksByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get tasks by status for a project", description = "Returns tasks with a specific status for a project")
    public ResponseEntity<List<TaskDto.Response>> getTasksByProjectIdAndStatus(
            @PathVariable Long projectId,
            @PathVariable Task.Status status) {
        return ResponseEntity.ok(taskService.getTasksByProjectIdAndStatus(projectId, status));
    }

    @GetMapping("/due-before")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get tasks due before date", description = "Returns tasks that are due before a specific date/time")
    public ResponseEntity<List<TaskDto.Response>> getTasksDueBeforeDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return ResponseEntity.ok(taskService.getTasksDueBeforeDate(dateTime));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update task", description = "Updates an existing task")
    public ResponseEntity<TaskDto.Response> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDto.Request request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update task status", description = "Updates only the status of a task")
    public ResponseEntity<TaskDto.Response> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam Task.Status status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete task", description = "Deletes a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}