package com.tpt.hotel_management.controller;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Booking;
import com.tpt.hotel_management.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveBooking(
            @RequestBody Booking booking,
            @RequestParam(name = "userId", required = true) Long userId,
            @RequestParam(name = "roomId", required = true) Long roomId
    ) throws Exception {
        return new ResponseEntity<>(
                this.bookingService.saveBooking(roomId, userId, booking),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/find-by-confirmation")
    public ResponseEntity<ApiResponse> findBookingByConfirmationCode(
            @RequestParam(name = "confirmationCode", required = true) String confirmationCode
    ) throws Exception {
        return new ResponseEntity<>(
                this.bookingService.findBookingByConfirmationCode(confirmationCode),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllBookings() throws Exception {
        return new ResponseEntity<>(
                this.bookingService.getAllBookings(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBookingById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(
                this.bookingService.getBookingById(id),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(
                this.bookingService.cancelBooking(id),
                HttpStatus.CREATED
        );
    }
}
