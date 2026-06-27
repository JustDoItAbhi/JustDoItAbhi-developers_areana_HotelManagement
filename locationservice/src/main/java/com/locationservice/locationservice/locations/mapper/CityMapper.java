package com.locationservice.locationservice.locations.mapper;


import com.locationservice.locationservice.locations.dto.response.CityNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CityResponseDto;
import com.locationservice.locationservice.locations.model.City;
import org.modelmapper.ModelMapper;

public class CityMapper {
    private final ModelMapper mapper;

    public CityMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CityNameResponseDto fromCityEntity(City city){
        CityNameResponseDto dto=mapper.map(city,CityNameResponseDto.class);
        return dto;
    }
    public static CityResponseDto fromCityByState(City city){
        CityResponseDto dto=new CityResponseDto();
        dto.setCityid(city.getId());
        dto.setCityName(city.getCityName());
        dto.setCityImage(city.getCityImage());
        return dto;
    }
}
