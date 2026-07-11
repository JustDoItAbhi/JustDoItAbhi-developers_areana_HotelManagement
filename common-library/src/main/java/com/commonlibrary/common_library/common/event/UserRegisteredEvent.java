package com.commonlibrary.common_library.common.event;

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
public class UserRegisteredEvent {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    @Builder.Default
    private Instant registeredAt = Instant.now();
}
