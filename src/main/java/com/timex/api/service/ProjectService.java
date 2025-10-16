package com.timex.api.service;

import com.timex.api.dto.ProjectDto;

import java.util.List;

public interface ProjectService {

    ProjectDto.Response createProject(String username, ProjectDto.Request request);

    ProjectDto.Response getProjectById(Long id);

    ProjectDto.DetailedResponse getProjectWithTasks(Long id);

    List<ProjectDto.Response> getAllProjects(String username);

    ProjectDto.Response updateProject(Long id, ProjectDto.Request request);

    void deleteProject(Long id);

    boolean existsByNameAndUsername(String name, String username);
}