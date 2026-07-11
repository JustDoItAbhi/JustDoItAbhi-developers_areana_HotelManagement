package com.locationservice.locationservice.locations.controller;


import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import com.locationservice.locationservice.locations.dto.request.StateRequestDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateResponseDto;
import com.locationservice.locationservice.locations.service.stateservice.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/location/state")
public class StateController {
    @Autowired
    private StateService stateService;

    @PostMapping
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<StateNameResponseDto>create(@RequestBody StateRequestDto dto){
        return ResponseEntity.ok(stateService.createState(dto));
    }

    @GetMapping
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<List<StateNameResponseDto>> getAll(){
        return ResponseEntity.ok(stateService.findAll());
    }
    @GetMapping("/getStateByCountryId/{countryId}/{stateName}")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<StateResponseDto>getStateAndCitiesByCountryIdAndStateName(@PathVariable ("countryId")UUID id, @PathVariable ("stateName") String stateName){
        return ResponseEntity.ok(stateService.getstateByCountryIdAndStateName(id,stateName));
    }
}
