package com.timex.api.mapper;

import com.timex.api.dto.TaskDto;
import com.timex.api.model.Project;
import com.timex.api.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "project", ignore = true)
    Task toEntity(TaskDto.Request taskDto);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    TaskDto.Response toDto(Task task);

    @Named("projectToId")
    default Long projectToId(Project project) {
        return project != null ? project.getId() : null;
    }

    @Named("projectToName")
    default String projectToName(Project project) {
        return project != null ? project.getName() : null;
    }
}