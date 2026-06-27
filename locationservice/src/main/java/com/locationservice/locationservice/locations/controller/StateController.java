package com.locationservice.locationservice.locations.controller;


import com.locationservice.locationservice.locations.dto.request.StateRequestDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.service.stateservice.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/state")
public class StateController {
    @Autowired
    private StateService stateService;

    @PostMapping
    public ResponseEntity<StateNameResponseDto>create(@RequestBody StateRequestDto dto){
        return ResponseEntity.ok(stateService.createState(dto));
    }

    @GetMapping
    public ResponseEntity<List<StateNameResponseDto>> getAll(){
        return ResponseEntity.ok(stateService.findAll());
    }
}
