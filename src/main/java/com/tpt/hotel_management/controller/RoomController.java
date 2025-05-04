package com.tpt.hotel_management.controller;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Room;
import com.tpt.hotel_management.service.BookingService;
import com.tpt.hotel_management.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> addRoom(
            @RequestParam(name = "photo", required = true) MultipartFile photo,
            @RequestParam(name = "roomType", required = true) String roomType,
            @RequestParam(name = "roomPrice", required = true) BigDecimal roomPrice,
            @RequestParam(name = "description", required = true) String description
    ) throws Exception {
        return new ResponseEntity<ApiResponse>(
                this.roomService.addNewRoom(photo, roomType, roomPrice, description),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateRoom(
            @PathVariable Long id,
            @RequestParam(name = "photo", required = false) MultipartFile photo,
            @RequestParam(name = "roomType", required = false) String roomType,
            @RequestParam(name = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(name = "description", required = false) String description
    ) throws Exception {
        return new ResponseEntity<ApiResponse>(
                this.roomService.updateRoom(id, photo, roomType, roomPrice, description),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/room-type")
    public ResponseEntity<ApiResponse> getRoomType() throws Exception {
        return new ResponseEntity<>(
                this.roomService.getAllRoomTypes(),
                HttpStatus.OK
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllRooms() throws Exception {
        return new ResponseEntity<>(
                this.roomService.getAllRooms(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(
                this.roomService.getRoomById(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/get-all-available-rooms")
    public ResponseEntity<ApiResponse> getAllAvailableRooms() throws Exception {
        return new ResponseEntity<>(
                this.roomService.getAllAvailableRooms(),
                HttpStatus.OK
        );
    }

    @GetMapping("/get-available-rooms")
    public ResponseEntity<ApiResponse> getAvailableRoomsByDateAndType(
            @RequestParam(name = "checkInDate")LocalDate checkInDate,
            @RequestParam(name = "checkOutDate")LocalDate checkOutDate,
            @RequestParam(name = "roomType") String roomType
            ) throws Exception {
        return new ResponseEntity<>(
                this.roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRoomById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(
                this.roomService.deleteRoom(id),
                HttpStatus.OK
        );
    }
}
