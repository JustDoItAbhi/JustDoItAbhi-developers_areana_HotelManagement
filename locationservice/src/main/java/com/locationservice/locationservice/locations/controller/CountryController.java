package com.locationservice.locationservice.locations.controller;


import com.locationservice.locationservice.locations.util.Tracking;
import com.locationservice.locationservice.locations.dto.request.CountryRequestDto;
import com.locationservice.locationservice.locations.dto.response.CountryOnlyNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CountryResponseDto;
import com.locationservice.locationservice.locations.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    @Autowired
    private CountryService countryService;
    @PostMapping
    @Tracking
    public ResponseEntity<CountryOnlyNameResponseDto>create(@RequestBody CountryRequestDto dto){
        return ResponseEntity.ok(countryService.createCountry(dto));
    }
    @GetMapping
    @Tracking
    public ResponseEntity<List<CountryOnlyNameResponseDto>> create(){
        return ResponseEntity.ok(countryService.getAllCountryNames());
    }
    @Tracking
    @GetMapping("byCountry/{countryName}")
    public ResponseEntity<List<CountryResponseDto>>getAllByCountryName(@PathVariable("countryName")String coountryName){
        return ResponseEntity.ok(countryService.getAllCountriesAndStatesAndCities(coountryName));
    }
}
