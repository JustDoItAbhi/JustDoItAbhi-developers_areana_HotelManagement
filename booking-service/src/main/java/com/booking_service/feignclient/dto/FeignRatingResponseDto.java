package com.booking_service.feignclient.dto;

import lombok.Data;

@Data
public class FeignRatingResponseDto {
    private int star;
    private String review;
}
