package com.user_service.customer.dto.response;

import com.commonlibrary.common_library.common.enums.UserRole;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String country;
    private String state;
    private String city;
    private String phoneNumber;
    private UserRole role;
}
