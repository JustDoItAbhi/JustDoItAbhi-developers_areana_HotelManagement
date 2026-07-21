package com.commonlibrary.common_library.common.event;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private UUID bookingId;
    private UUID userId;
    private String userEmail;
    private String userName;
    private Double amount;
    private String currency;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private UUID roomId;
    private RoomType roomType;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer numberOfGuests;
    private Boolean isSuccessful;
    private String failureReason;
    private String hotelName;
    @Builder.Default
    private LocalDateTime completedAt = LocalDateTime.now();
}
