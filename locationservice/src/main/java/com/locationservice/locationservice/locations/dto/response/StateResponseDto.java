package com.locationservice.locationservice.locations.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class StateResponseDto {
    private UUID stateId;
    private String stateName;
    private List<CityResponseDto>cityResponseDtoList;
}
