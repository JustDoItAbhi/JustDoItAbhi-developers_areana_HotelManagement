package com.locationservice.locationservice.locations.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CountryResponseDto {
    private UUID countryId;
    private String countryName;
    private List<StateResponseDto> stateResponseDtosList;
}
