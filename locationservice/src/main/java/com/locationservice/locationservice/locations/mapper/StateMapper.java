package com.locationservice.locationservice.locations.mapper;


import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.model.States;

public class StateMapper {
    public static StateNameResponseDto fromState(States state){
        StateNameResponseDto dto=new StateNameResponseDto();
        dto.setStateId(state.getId());
        dto.setCountryId(state.getCountry().getId());
        dto.setStateName(state.getStateName());
        dto.setCountryName(state.getCountry().getCountryName());
        return dto;
    }
}
