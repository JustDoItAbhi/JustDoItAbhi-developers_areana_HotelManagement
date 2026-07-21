package com.commonlibrary.common_library.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data

@NoArgsConstructor
@AllArgsConstructor
public class HotelCreatedEvent {
    private UUID hotelId;
    private String hotelName;
    private String description;
}
