package com.inventory_service.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Entity
@Getter
@Setter
public class UserInventry extends BaseModel {
    private String userEmail;
    private String hotelName;
    private UUID roomId;
    private double amount;
    private int availableQuantity;
    private int reservedQuantity;
    private UUID bookingId;
    private String reason;
}
