package com.locationservice.locationservice.locations.service.stateservice;

import com.locationservice.locationservice.locations.dto.request.StateRequestDto;
import com.locationservice.locationservice.locations.dto.response.StateNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateResponseDto;
import com.locationservice.locationservice.locations.mapper.StateMapper;
import com.locationservice.locationservice.locations.model.Country;
import com.locationservice.locationservice.locations.model.States;
import com.locationservice.locationservice.locations.repository.CityRepository;
import com.locationservice.locationservice.locations.repository.CountryRepository;
import com.locationservice.locationservice.locations.repository.StateRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class StateServiceImpl implements StateService{
    Map<String, UUID> cityMap=new ConcurrentHashMap<>();
    Map<String, UUID>stateMap=new ConcurrentHashMap<>();
    Map<String, UUID>countryMap=new ConcurrentHashMap<>();


    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;


    public StateServiceImpl(CountryRepository countryRepository, CityRepository cityRepository, StateRepository stateRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
    }

    @PostConstruct
    public void initialize() {

        List<States> statesList = stateRepository.findAllWithCountry();

        for (States state : statesList) {
            String key = state.getCountry().getCountryName() + ":" + state.getStateName();
            stateMap.put(key, state.getId());
        }
    }
    @Override
    public StateNameResponseDto createState(StateRequestDto dto) {
        String stateKey = dto.getCountryName() + ":" + dto.getStateName();

        UUID existingStateId = stateMap.get(stateKey);

        if (existingStateId != null) {
            States state = stateRepository.getReferenceById(existingStateId);
            return StateMapper.fromState(state);
        }

        Country country = countryRepository.findByCountryName(dto.getCountryName())
                        .orElseThrow(() -> new RuntimeException("Country not found"));
        States state = new States();
        state.setStateName(dto.getStateName());
        state.setCountry(country);
        state = stateRepository.save(state);
        stateMap.put(stateKey, state.getId());
        return StateMapper.fromState(state);
    }

    @Override
    public List<StateNameResponseDto> findAll() {

            List<States>countryList=stateRepository.findAll();
            List<StateNameResponseDto>responseDtos=new ArrayList<>();
            for(States states:countryList){
                responseDtos.add(StateMapper.fromState(states));
            }
            return responseDtos;
        }

    @Override
    public StateResponseDto getstateByCountryIdAndStateName(UUID countryId,String stateName) {
        Optional<States>statesOptional=stateRepository.findByCountryIdAndStateName(countryId,stateName);
        if(statesOptional.isEmpty()){
            throw new RuntimeException("INVALID COUNTRY ID AND STATE NAME "+countryId+" "+stateName);
        }
        return StateMapper.fromStateToStateResponseDto(statesOptional.get());
    }

}
