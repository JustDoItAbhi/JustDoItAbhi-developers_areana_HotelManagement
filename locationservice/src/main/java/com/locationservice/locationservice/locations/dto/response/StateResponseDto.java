package com.locationservice.locationservice.locations.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class StateResponseDto {
    private List<CityResponseDto>cityResponseDtoList;
    private UUID stateId;
    private String stateName;

}
