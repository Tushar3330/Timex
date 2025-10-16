package com.timex.api.mapper;

import com.timex.api.dto.ProjectDto;
import com.timex.api.model.Project;
import com.timex.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { TaskMapper.class })
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project toEntity(ProjectDto.Request projectDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    ProjectDto.Response toDto(Project project);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "tasks", source = "tasks")
    ProjectDto.DetailedResponse toDetailedDto(Project project);

    @Named("userToId")
    default Long userToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("userToName")
    default String userToName(User user) {
        return user != null ? user.getUsername() : null;
    }
}