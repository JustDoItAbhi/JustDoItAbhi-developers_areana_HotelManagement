package com.notification_service.dto;

import com.commonlibrary.common_library.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String country;
    private String state;
    private String city;
    private String phoneNumber;
    private UserRole role;
}
