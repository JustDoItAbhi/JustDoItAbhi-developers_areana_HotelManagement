package com.locationservice.locationservice.locations.controller;

import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import com.locationservice.locationservice.locations.dto.request.CityRequestDto;
import com.locationservice.locationservice.locations.dto.response.CityNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CityResponseDto;
import com.locationservice.locationservice.locations.service.cityservice.CityService;
import com.locationservice.locationservice.locations.util.Tracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<CityNameResponseDto> create(@RequestBody CityRequestDto dto) {
        return ResponseEntity.ok(cityService.createCity(dto));
    }

    @GetMapping
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<List<CityNameResponseDto>> all(){
        return ResponseEntity.ok(cityService.getAll());
    }
    @GetMapping("/cityByState/{statename}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<List<CityResponseDto>> allbyStateName(@PathVariable ("statename")String stateName){
        return ResponseEntity.ok(cityService.findByStateName(stateName));
    }
    @GetMapping("/cityByName/{cityName}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<CityResponseDto> getVyCityName(@PathVariable ("cityName")String cityName){
        return ResponseEntity.ok(cityService.getByCityName(cityName));
    }

}
