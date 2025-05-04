package com.tpt.hotel_management.utils;

import com.tpt.hotel_management.dto.BookingDTO;
import com.tpt.hotel_management.dto.RoomDTO;
import com.tpt.hotel_management.dto.UserDTO;
import com.tpt.hotel_management.entity.Booking;
import com.tpt.hotel_management.entity.Room;
import com.tpt.hotel_management.entity.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKMLNOPQRSWUTVYZ0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = RANDOM.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(number));
        }
        return sb.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        return userDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        return roomDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumberOfAdults(booking.getNumberOfAdults());
        bookingDTO.setNumberOfChildren(booking.getNumberOfChildren());
        bookingDTO.setTotalNumberOfGuests(booking.getTotalNumberOfGuests());
        return bookingDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());
        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return roomDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingAndRoom(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setName(user.getName());
        if(!user.getBookings().isEmpty()){
            userDTO.setBookings(user.getBookings().stream().map(b -> mapBookingEntityToBookingDTOPlusBookedRoom(b, false)).toList());
        }
        return userDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRoom(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumberOfAdults(booking.getNumberOfAdults());
        bookingDTO.setNumberOfChildren(booking.getNumberOfChildren());
        bookingDTO.setTotalNumberOfGuests(booking.getTotalNumberOfGuests());
        if(mapUser){
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
        if(booking.getRoom() != null){
            bookingDTO.setRoom(Utils.mapRoomEntityToRoomDTO(booking.getRoom()));
        }
        return bookingDTO;
    }

    public static List<UserDTO> mapUserEntityListToUserDTOList(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).toList();
    }

    public static List<RoomDTO> mapRoomEntityListToRoomDTOList(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).toList();
    }

    public static List<BookingDTO> mapBookingEntityListToBookingDTOList(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).toList();
    }
}
