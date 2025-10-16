package com.timex.api.mapper;

import com.timex.api.dto.UserDto;
import com.timex.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDto.Request userDto);

    @Mapping(target = "roles", source = "roles")
    UserDto.Response toDto(User user);
}