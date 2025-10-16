package com.timex.api.service;

import com.timex.api.dto.UserDto;
import com.timex.api.model.User;

import java.util.List;

public interface UserService {

    UserDto.Response registerUser(UserDto.Request request);

    UserDto.LoginResponse authenticateUser(UserDto.LoginRequest loginRequest);

    UserDto.Response getUserProfile(String username);

    List<UserDto.Response> getAllUsers();

    UserDto.Response getUserById(Long id);

    UserDto.Response updateUser(Long id, UserDto.Request request);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}