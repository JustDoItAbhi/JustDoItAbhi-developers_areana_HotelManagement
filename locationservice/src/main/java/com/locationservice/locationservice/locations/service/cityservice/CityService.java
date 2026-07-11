package com.locationservice.locationservice.locations.service.cityservice;


import com.locationservice.locationservice.locations.dto.request.CityRequestDto;
import com.locationservice.locationservice.locations.dto.response.CityNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CityResponseDto;

import java.util.List;
import java.util.UUID;

public interface CityService {
    CityNameResponseDto createCity(CityRequestDto dto);
    List<CityNameResponseDto>getAll();
    List<CityResponseDto>findByStateName(String stateName);
    CityResponseDto getByCityName(String cityName);
}
