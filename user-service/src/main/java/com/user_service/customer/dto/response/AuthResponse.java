package com.user_service.customer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String token ;
    private UUID userid;
    private String userName;
}
