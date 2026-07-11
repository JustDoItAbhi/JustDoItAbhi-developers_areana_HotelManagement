package com.booking_service.feignclient;

import lombok.Data;

@Data
public class FeignAddressResponseDto {
    private String countryName;
    private String  stateName;
    private String  cityName;
    private String street;
    private String buildingNumber;
    private String zipCode;
    private String nearByLocation;
}
