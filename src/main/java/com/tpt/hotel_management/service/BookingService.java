package com.tpt.hotel_management.service;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Booking;

public interface BookingService {
    ApiResponse saveBooking(Long roomId, Long userId, Booking booking) throws Exception;
    ApiResponse findBookingByConfirmationCode(String confirmationCode) throws Exception;
    ApiResponse getAllBookings() throws Exception;
    ApiResponse getBookingById(Long id) throws Exception;
    ApiResponse cancelBooking(Long id) throws Exception;
}
