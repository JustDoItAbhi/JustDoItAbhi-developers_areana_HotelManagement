package com.locationservice.locationservice.locations.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class StateNameResponseDto {
    private UUID stateId;
    private String countryName;
    private UUID countryId;
    private String stateName;
}
