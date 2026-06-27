package com.locationservice.locationservice.locations.service.stateservice;



import com.locationservice.locationservice.locations.dto.request.StateRequestDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;

import java.util.List;

public interface StateService {
    StateNameResponseDto createState(StateRequestDto dto);
    List<StateNameResponseDto>findAll();
}
