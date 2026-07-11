package com.locationservice.locationservice.locations.service;

import com.locationservice.locationservice.locations.dto.request.CountryRequestDto;
import com.locationservice.locationservice.locations.dto.response.CountryOnlyNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CountryResponseDto;

import java.util.List;
import java.util.UUID;

public interface CountryService {
    CountryOnlyNameResponseDto createCountry(CountryRequestDto dto);
    boolean deleteCountry(UUID countryId);
    CountryResponseDto updateCountryName(String countryName);
    List<CountryResponseDto>getAllCountriesAndStatesAndCities(String countryName);
    List<CountryOnlyNameResponseDto>getAllCountryNames();
    CountryResponseDto getByCountryName(String name);

}
