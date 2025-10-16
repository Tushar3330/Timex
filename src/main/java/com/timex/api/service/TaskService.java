package com.timex.api.service;

import com.timex.api.dto.TaskDto;
import com.timex.api.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    TaskDto.Response createTask(TaskDto.Request request);

    TaskDto.Response getTaskById(Long id);

    List<TaskDto.Response> getTasksByProjectId(Long projectId);

    List<TaskDto.Response> getTasksByProjectIdAndStatus(Long projectId, Task.Status status);

    List<TaskDto.Response> getTasksDueBeforeDate(LocalDateTime dateTime);

    TaskDto.Response updateTask(Long id, TaskDto.Request request);

    TaskDto.Response updateTaskStatus(Long id, Task.Status status);

    void deleteTask(Long id);
}