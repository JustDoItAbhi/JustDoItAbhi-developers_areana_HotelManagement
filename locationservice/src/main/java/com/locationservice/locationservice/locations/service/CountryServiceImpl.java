package com.locationservice.locationservice.locations.service;


import com.commonlibrary.common_library.common.redis.RedisService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationservice.locationservice.locations.dto.request.CountryRequestDto;
import com.locationservice.locationservice.locations.dto.response.CityResponseDto;
import com.locationservice.locationservice.locations.dto.response.CountryOnlyNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CountryResponseDto;
import com.locationservice.locationservice.locations.dto.response.StateResponseDto;
import com.locationservice.locationservice.locations.model.City;
import com.locationservice.locationservice.locations.model.Country;
import com.locationservice.locationservice.locations.model.States;
import com.locationservice.locationservice.locations.repository.CityRepository;
import com.locationservice.locationservice.locations.repository.CountryRepository;
import com.locationservice.locationservice.locations.repository.StateRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    Map<String,UUID>countryMap=new ConcurrentHashMap<>();
    Map<String, UUID>cityMap=new ConcurrentHashMap<>();
    Map<String, UUID>stateMap=new ConcurrentHashMap<>();
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final ModelMapper mapper;

    public CountryServiceImpl(CountryRepository countryRepository, CityRepository cityRepository, StateRepository stateRepository, ModelMapper mapper) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.mapper = mapper;
    }
    @PostConstruct
    public void initialize() {
      List<Country> country=  countryRepository.findAll();
      for(Country country1:country){
          countryMap.put(country1.getCountryName(),country1.getId());
      }
        System.out.println("COUNTRY CACHES ADDED ");
    }

    @Override
    public CountryOnlyNameResponseDto createCountry(CountryRequestDto dto) {
        UUID existingId = countryMap.get(dto.getCountryName());

        if (existingId != null) {
            Country oldCountry = countryRepository.getReferenceById(existingId);
            String key="country:"+oldCountry.getId();
            Object cache=redisService.get(key);
            if(cache!=null){
                return objectMapper.convertValue(cache,CountryOnlyNameResponseDto.class);
            }

            return mapper.map(oldCountry, CountryOnlyNameResponseDto.class);
        }

            Country country = new Country();
            country.setCountryName(dto.getCountryName());

            country = countryRepository.save(country);

            countryMap.put(country.getCountryName(), country.getId());
            CountryOnlyNameResponseDto responseDto=mapper.map(country, CountryOnlyNameResponseDto.class);
            redisService.set("country:"+country.getId(),responseDto);
            return responseDto;
        }


    @Override
    public boolean deleteCountry(UUID countryId) {
        String key="country:"+countryId;
        countryRepository.deleteById(countryId);
        redisService.delete(key);
        return true;
    }

    @Override
    public CountryResponseDto updateCountryName(String countryName) {
        return null;
    }

    @Override
    public List<CountryResponseDto> getAllCountriesAndStatesAndCities(String countryName) {
     String key ="allcountrysAndCities";
     Object cache=redisService.get(key);
     if(cache!=null){
         JavaType type = objectMapper.getTypeFactory()
                 .constructCollectionType(List.class, CountryResponseDto.class);
         return objectMapper.convertValue(cache, type);
     }
      if(!cityMap.containsKey(countryName)){
          System.out.println("COUNTRY NAME IS INVALID IN CACHE MEMORY "+countryName);
      }
      List<StateResponseDto>stateResponseDtos=new ArrayList<>();
        List<CountryResponseDto>responseDtos=new ArrayList<>();
        Optional<Country>countryOptional=countryRepository.findByCountryName(countryName);
        if(countryOptional.isEmpty()){
            throw new RuntimeException("INVALID COUNTRY NAME "+countryName);
        }
        CountryResponseDto countryResponseDto=new CountryResponseDto();
        countryResponseDto.setCountryId(countryOptional.get().getId());
        countryResponseDto.setCountryName(countryOptional.get().getCountryName());
        List<States>statesList=stateRepository.findAllStatesWithCitiesByCountry(countryName);
        for(States newState:statesList){
            List<CityResponseDto>cityResponseDtoList=new ArrayList<>();
            StateResponseDto stateResponseDto=new StateResponseDto();
            stateResponseDto.setStateName(newState.getStateName());
            stateResponseDto.setStateId(newState.getId());
            String stateKey="state:"+newState.getId();
            Object stateCache=redisService.get(stateKey);
            if(stateCache==null){
                redisService.set(stateKey,stateResponseDto,1, TimeUnit.HOURS);
            }
            for(City city:newState.getCityList()){
                CityResponseDto cityResponseDto=new CityResponseDto();
                cityResponseDto.setCityid(city.getId());
                cityResponseDto.setCityName(city.getCityName());
                cityResponseDto.setCityImage(city.getCityImage());
                cityResponseDtoList.add(cityResponseDto);
                String cityKey="city:"+city.getId();
                Object cityCache=redisService.get(cityKey);
                if(cityCache==null){
                    redisService.set(cityKey,cityResponseDto,1,TimeUnit.HOURS);
                }
            }
            if(stateCache!=null){

            }
            stateResponseDto.setCityResponseDtoList(cityResponseDtoList);
            stateResponseDtos.add(stateResponseDto);
        }
        countryResponseDto.setStateResponseDtosList(stateResponseDtos);
        List<CountryResponseDto> countryResponseDtoList=new ArrayList<>();
            countryResponseDtoList.add(countryResponseDto);
        return countryResponseDtoList;
    }



    @Override
    public List<CountryOnlyNameResponseDto> getAllCountryNames() {
        List<Country>countryList=countryRepository.findAll();
        List<CountryOnlyNameResponseDto>responseDtos=new ArrayList<>();
        for(Country country:countryList){
            responseDtos.add(mapper.map(country,CountryOnlyNameResponseDto.class));
        }
        return responseDtos;
    }

    @Override
    public CountryResponseDto getByCountryName(String name) {
        String key="countryname:"+name;
        Object cache=redisService.get(key);
        if(cache!=null){
            return objectMapper.convertValue(cache,CountryResponseDto.class);
        }
        Optional<Country>countryOptional=countryRepository.findByCountryName(name);
        if(countryOptional.isEmpty()){
            throw new RuntimeException("country with name not exists "+name);
        }
        CountryResponseDto dto=mapper.map(countryOptional.get(),CountryResponseDto.class);
        redisService.set(key,dto,1,TimeUnit.HOURS);
        return dto;
    }
}
