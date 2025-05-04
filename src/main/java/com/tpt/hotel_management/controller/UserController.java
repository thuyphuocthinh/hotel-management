package com.tpt.hotel_management.controller;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.service.UserService;
import com.tpt.hotel_management.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final JwtUtils jwtUtils;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() throws Exception {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(this.userService.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/booking-history")
    public ResponseEntity<ApiResponse> getUserBookingHistory(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(this.userService.getUserBookingHistory(id), HttpStatus.OK);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<ApiResponse> getMyInfo(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        String jwt = token.substring(7);
        String email = this.jwtUtils.extractUsername(jwt);
        return new ResponseEntity<>(this.userService.getMyInfo(email), HttpStatus.OK);
    }
}
