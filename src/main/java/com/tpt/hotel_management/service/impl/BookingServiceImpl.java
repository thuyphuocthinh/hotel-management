package com.tpt.hotel_management.service.impl;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Booking;
import com.tpt.hotel_management.entity.Room;
import com.tpt.hotel_management.entity.User;
import com.tpt.hotel_management.exception.OurException;
import com.tpt.hotel_management.repository.BookingRepository;
import com.tpt.hotel_management.repository.RoomRepository;
import com.tpt.hotel_management.repository.UserRepository;
import com.tpt.hotel_management.service.BookingService;
import com.tpt.hotel_management.service.RoomService;
import com.tpt.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final RoomService roomService;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Override
    public ApiResponse saveBooking(Long roomId, Long userId, Booking booking) throws Exception {
        try {
            if(booking.getCheckOutDate().isBefore(booking.getCheckInDate())){
                throw new IllegalArgumentException("Check in date should be before check out date");
            }
            Room room = this.roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));

            User user = this.userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            List<Booking> existingBookings = room.getBookings();
            if(!roomAvailable(booking, existingBookings)) {
                throw new OurException("Room is not available");
            }

            booking.setRoom(room);
            booking.setUser(user);
            booking.setBookingConfirmationCode(Utils.generateRandomString(10));
            Booking savedBooking = this.bookingRepository.save(booking);

            return ApiResponse.builder()
                    .data(Utils.mapBookingEntityToBookingDTO(savedBooking))
                    .status("Success")
                    .message("Booking saved successfully")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error save booking");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    private boolean roomAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (
                                        bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                )
                );
    }

    @Override
    public ApiResponse findBookingByConfirmationCode(String confirmationCode) throws Exception {
        try {
            return ApiResponse.builder()
                    .message("Find bookings successfully")
                    .status("Success")
                    .data(Utils.mapBookingEntityToBookingDTO(this.bookingRepository.findByBookingConfirmationCode(confirmationCode)))
                    .build();
        } catch (OurException e) {
            throw new OurException("Find booking by confirmation code failed");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getAllBookings() throws Exception {
        try {
            return ApiResponse.builder()
                    .message("Find bookings successfully")
                    .status("Success")
                    .data(Utils.mapBookingEntityListToBookingDTOList(bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))))
                    .build();
        } catch (OurException e) {
            throw new OurException("Find booking by confirmation code failed");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getBookingById(Long id) throws Exception {
        try {
            return ApiResponse.builder()
                    .message("Find bookings successfully")
                    .status("Success")
                    .data(Utils.mapBookingEntityToBookingDTO(this.bookingRepository.findById(id).orElseThrow(() -> new Exception("Booking not found"))))
                    .build();
        } catch (OurException e) {
            throw new OurException("Find booking by confirmation code failed");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse cancelBooking(Long id) throws Exception {
        try {
            Booking booking = this.bookingRepository.findById(id)
                    .orElseThrow(() -> new OurException("Booking not found"));
            this.bookingRepository.delete(booking);
            return ApiResponse.builder()
                    .message("Canceled booking successfully")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error cancel booking");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }
}
