package com.locationservice.locationservice.locations.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CityRequestDto {
    private UUID countryId;
    private String countryName;
    private String stateName;
    private List<CityDetailRequestDto> cityDetailRequestDtoList;

}
