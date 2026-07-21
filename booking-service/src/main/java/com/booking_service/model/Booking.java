package com.booking_service.model;

import com.commonlibrary.common_library.common.enums.RoomType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Booking  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String userEmail;
    private UUID hotelId;
    private UUID roomId;
    private RoomType roomType;
    private LocalDateTime checkInDate=LocalDateTime.now();
    private LocalDateTime checkOutDate=LocalDateTime.now();
    private double totalAmount;
    private String status;
    private String url;
}
