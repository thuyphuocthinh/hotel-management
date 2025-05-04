package com.tpt.hotel_management.service.impl;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.dto.LoginRequest;
import com.tpt.hotel_management.dto.UserDTO;
import com.tpt.hotel_management.entity.User;
import com.tpt.hotel_management.exception.OurException;
import com.tpt.hotel_management.repository.UserRepository;
import com.tpt.hotel_management.service.UserService;
import com.tpt.hotel_management.utils.JwtUtils;
import com.tpt.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse register(User user) throws Exception {
        try {
            if(user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            return ApiResponse.builder()
                    .data(userDTO)
                    .message("User registered successfully")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Registration failed");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) throws Exception {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            var user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User not found"));
            var jwt = jwtUtils.generateToken(user);
            return ApiResponse.builder()
                    .status("Success")
                    .data(jwt)
                    .message("User logged in successfully")
                    .build();
        } catch (OurException e) {
            throw new OurException("Login failed");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse getAllUsers() throws Exception {
        try {
            return ApiResponse.builder()
                    .status("Success")
                    .message("Getting all users successfully")
                    .data(Utils.mapUserEntityListToUserDTOList(userRepository.findAll()))
                    .build();
        } catch (OurException e) {
            throw new OurException("Error getting all users");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse getUserBookingHistory(Long id) throws Exception {
        try {
            return ApiResponse.builder()
                    .status("Success")
                    .message("Getting user booking history successfully")
                    .data(Utils.mapUserEntityToUserDTOPlusUserBookingAndRoom(userRepository.findById(id).orElseThrow(() -> new OurException("User not found"))))
                    .build();
        } catch (OurException e) {
            throw new OurException("Error getting user booking history");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse deleteUser(Long id) throws Exception {
        try {
            userRepository.findById(id).orElseThrow(() -> new OurException("User not found"));
            userRepository.deleteById(id);
            return ApiResponse.builder()
                    .status("Success")
                    .message("Deleted user successfully")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error delete user");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse getUserById(Long id) throws Exception {
        try {
            return ApiResponse.builder()
                    .status("Success")
                    .message("Getting user detail successfully")
                    .data(Utils.mapUserEntityToUserDTO(userRepository.findById(id).orElseThrow(() -> new OurException("User not found"))))
                    .build();
        } catch (OurException e) {
            throw new OurException("Error getting user detail");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }

    @Override
    public ApiResponse getMyInfo(String email) throws Exception {
        try {
            return ApiResponse.builder()
                    .status("Success")
                    .message("Getting my info successfully")
                    .data(Utils.mapUserEntityToUserDTO(userRepository.findByEmail(email).orElseThrow(() -> new OurException("User not found"))))
                    .build();
        } catch (OurException e) {
            throw new OurException("Error getting info detail");
        } catch (Exception e) {
            throw new Exception("Server Error");
        }
    }
}
