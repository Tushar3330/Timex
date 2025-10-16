package com.timex.api.controller;

import com.timex.api.dto.UserDto;
import com.timex.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<UserDto.Response> registerUser(@Valid @RequestBody UserDto.Request request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<UserDto.LoginResponse> authenticateUser(@Valid @RequestBody UserDto.LoginRequest request) {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }

    @GetMapping("/check-username")
    @Operation(summary = "Check username availability", description = "Checks if a username is already taken")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Checks if an email is already in use")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}