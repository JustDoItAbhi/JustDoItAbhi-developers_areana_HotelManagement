package com.locationservice.locationservice.locations.service.stateservice;



import com.locationservice.locationservice.locations.dto.request.StateRequestDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateResponseDto;

import java.util.List;
import java.util.UUID;

public interface StateService {
    StateNameResponseDto createState(StateRequestDto dto);
    List<StateNameResponseDto>findAll();
    StateResponseDto getstateByCountryIdAndStateName(UUID countryId, String stateName);
}
