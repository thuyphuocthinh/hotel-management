package com.tpt.hotel_management.service;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.dto.LoginRequest;
import com.tpt.hotel_management.dto.UserDTO;
import com.tpt.hotel_management.entity.User;

public interface UserService {
    ApiResponse register(User user) throws Exception;
    ApiResponse login(LoginRequest loginRequest) throws Exception;
    ApiResponse getAllUsers() throws Exception;
    ApiResponse getUserBookingHistory(Long id) throws Exception;
    ApiResponse deleteUser(Long id) throws Exception;
    ApiResponse getUserById(Long id) throws Exception;
    ApiResponse getMyInfo(String email) throws Exception;
}
