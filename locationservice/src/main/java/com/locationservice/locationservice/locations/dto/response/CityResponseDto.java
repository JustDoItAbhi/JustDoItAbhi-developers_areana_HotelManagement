package com.locationservice.locationservice.locations.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CityResponseDto {
    private UUID cityid;
    private String cityName;
    private String cityImage;
}
