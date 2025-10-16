package com.timex.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timex.api.dto.UserDto;
import com.timex.api.model.User;
import com.timex.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto.Request userRequest;
    private UserDto.Response userResponse;
    private UserDto.Response adminResponse;
    private List<UserDto.Response> allUsers;

    @BeforeEach
    void setUp() {
        // Setup test data
        Set<User.Role> userRoles = new HashSet<>();
        userRoles.add(User.Role.ROLE_USER);

        Set<User.Role> adminRoles = new HashSet<>();
        adminRoles.add(User.Role.ROLE_ADMIN);

        userRequest = UserDto.Request.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .fullName("Test User")
                .build();

        userResponse = UserDto.Response.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .roles(userRoles)
                .build();

        adminResponse = UserDto.Response.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .fullName("Admin User")
                .roles(adminRoles)
                .build();

        allUsers = Arrays.asList(userResponse, adminResponse);
    }

    @Test
    @DisplayName("Should get all users when admin")
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsersWhenAdmin() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(allUsers);

        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("admin")));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should return forbidden when non-admin tries to get all users")
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenNonAdminTriesToGetAllUsers() throws Exception {
        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAllUsers();
    }

    @Test
    @DisplayName("Should get user by ID")
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserById() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should update user")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUser() throws Exception {
        // Given
        when(userService.updateUser(eq(1L), any(UserDto.Request.class))).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")));

        verify(userService).updateUser(eq(1L), any(UserDto.Request.class));
    }

    @Test
    @DisplayName("Should delete user")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When & Then
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Should return forbidden when non-admin tries to delete user")
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenNonAdminTriesToDeleteUser() throws Exception {
        // When & Then
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteUser(anyLong());
    }
}