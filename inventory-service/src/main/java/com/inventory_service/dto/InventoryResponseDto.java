package com.inventory_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponseDto {
    private UUID id;
    private String userEmail;
    private String hotelName;
    private UUID roomID;
    private long amount;
}
