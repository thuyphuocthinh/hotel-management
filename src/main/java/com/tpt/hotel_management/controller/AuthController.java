package com.tpt.hotel_management.controller;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.dto.LoginRequest;
import com.tpt.hotel_management.entity.User;
import com.tpt.hotel_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(this.userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity<>(this.userService.login(loginRequest), HttpStatus.OK);
    }
}
