package com.commonlibrary.common_library.common.event;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInventoryUpdatedEvent {
    private UUID roomId;
    private RoomType roomType;
    private int availableRooms;
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
