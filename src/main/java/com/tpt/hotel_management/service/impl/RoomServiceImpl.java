package com.tpt.hotel_management.service.impl;

import com.tpt.hotel_management.dto.ApiResponse;
import com.tpt.hotel_management.entity.Room;
import com.tpt.hotel_management.exception.OurException;
import com.tpt.hotel_management.repository.RoomRepository;
import com.tpt.hotel_management.repository.UserRepository;
import com.tpt.hotel_management.service.RoomService;
import com.tpt.hotel_management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    private final AwsService awsService;
    private final UserRepository userRepository;

    @Override
    public ApiResponse addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws Exception {
        try {
            String imageUrl = this.awsService.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            room.setRoomPhotoUrl(imageUrl);
            Room savedRoom = this.roomRepository.save(room);
            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityToRoomDTO(savedRoom))
                    .message("Successfully added new room")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getAllRoomTypes() throws Exception {
       try {
            return ApiResponse.builder()
                    .data(roomRepository.findDistinctRoomType())
                    .message("Successfully retrieved room types")
                    .status("Success")
                    .build();
       } catch (OurException e) {
           throw new OurException("Error add new room");
       } catch (Exception e) {
           throw new Exception("Server error");
       }
    }

    @Override
    public ApiResponse getAllRooms() throws Exception {
        try {
            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityListToRoomDTOList(roomRepository.findAll()))
                    .message("Successfully retrieved all rooms")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error get all rooms");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getRoomById(Long id) throws Exception {
        try {
            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityToRoomDTO(roomRepository.findById(id).orElseThrow(() -> new Exception("Room not found"))))
                    .message("Successfully retrieved room by id")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws Exception {
        try {
            Room room = this.roomRepository.findById(roomId)
                    .orElseThrow(() -> new Exception("Room not found"));

            if(photo != null) {
                room.setRoomPhotoUrl(this.awsService.saveImageToS3(photo));
            }

            if(roomPrice != null) {
                room.setRoomPrice(roomPrice);
            }

            if(roomPrice != null) {
                room.setRoomPrice(roomPrice);
            }

            if(description != null) {
                room.setRoomDescription(description);
            }

            Room savedRoom = this.roomRepository.save(room);

            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityToRoomDTO(savedRoom))
                    .message("Successfully deleted room")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse deleteRoom(Long id) throws Exception {
        try {
            roomRepository.findById(id).orElseThrow(() -> new Exception("Room not found"));
            roomRepository.deleteById(id);
            return ApiResponse.builder()
                    .message("Successfully deleted room")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws Exception {
        try {
            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityListToRoomDTOList(roomRepository.findAvailableRoomsByDateAndRoomType(checkInDate, checkOutDate, roomType)))
                    .message("Successfully get available rooms by date and type")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    @Override
    public ApiResponse getAllAvailableRooms() throws Exception {
        try {
            return ApiResponse.builder()
                    .data(Utils.mapRoomEntityListToRoomDTOList(roomRepository.getAllAvailableRooms()))
                    .message("Successfully get all available rooms")
                    .status("Success")
                    .build();
        } catch (OurException e) {
            throw new OurException("Error add new room");
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }
}
