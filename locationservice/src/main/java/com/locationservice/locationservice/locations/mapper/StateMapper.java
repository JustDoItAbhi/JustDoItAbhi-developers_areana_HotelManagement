package com.locationservice.locationservice.locations.mapper;


import com.locationservice.locationservice.locations.dto.response.CityResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateResponseDto;
import com.locationservice.locationservice.locations.model.City;
import com.locationservice.locationservice.locations.model.States;

import java.util.ArrayList;
import java.util.List;

public class StateMapper {
    public static StateNameResponseDto fromState(States state){
        StateNameResponseDto dto=new StateNameResponseDto();
        dto.setStateId(state.getId());
        dto.setCountryId(state.getCountry().getId());
        dto.setStateName(state.getStateName());
        dto.setCountryName(state.getCountry().getCountryName());
        return dto;
    }
    public static StateResponseDto fromStateToStateResponseDto(States state){
        StateResponseDto dto=new StateResponseDto();
        dto.setStateId(state.getId());
        dto.setStateName(state.getStateName());
        List<CityResponseDto>cityResponseDtos=new ArrayList<>();
        for(City city:state.getCityList()){
            System.out.println("CITY DETAILS "+city.getCityName());
            cityResponseDtos.add(CityMapper.fromCityByState(city));
        }
        dto.setCityResponseDtoList(cityResponseDtos);
        return dto;
    }
}
