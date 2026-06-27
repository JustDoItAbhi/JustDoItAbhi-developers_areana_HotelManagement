package com.locationservice.locationservice.locations.mapper;

import com.locationservice.locationservice.locations.dto.response.CountryOnlyNameResponseDto;
import com.locationservice.locationservice.locations.model.Country;
import org.modelmapper.ModelMapper;

public class CountryMapper {

private final ModelMapper mapper;

    public CountryMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CountryOnlyNameResponseDto fromCountryEntity(Country country){
        CountryOnlyNameResponseDto dto=mapper.map(country,CountryOnlyNameResponseDto.class);
        return dto;
    }

}
