package com.tpt.hotel_management.service;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RoomService {
    ApiResponse addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws Exception;
    ApiResponse getAllRoomTypes() throws Exception;
    ApiResponse getAllRooms() throws Exception;
    ApiResponse getRoomById(Long id) throws Exception;
    ApiResponse updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws Exception;
    ApiResponse deleteRoom(Long id) throws Exception;
    ApiResponse getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws Exception;
    ApiResponse getAllAvailableRooms() throws Exception;
}
