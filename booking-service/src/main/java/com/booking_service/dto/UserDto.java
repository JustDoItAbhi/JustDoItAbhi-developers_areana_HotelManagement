package com.booking_service.dto;

import com.commonlibrary.common_library.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String country;
    private String state;
    private String city;
    private String phoneNumber;
    private String role;
}
