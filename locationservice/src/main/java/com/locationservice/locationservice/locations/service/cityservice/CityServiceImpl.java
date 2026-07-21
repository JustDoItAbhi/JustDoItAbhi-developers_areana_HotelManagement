package com.locationservice.locationservice.locations.service.cityservice;


import com.commonlibrary.common_library.common.redis.RedisService;
import com.locationservice.locationservice.locations.dto.request.CityDetailRequestDto;
import com.locationservice.locationservice.locations.dto.request.CityRequestDto;
import com.locationservice.locationservice.locations.dto.response.CityNameResponseDto;
import com.locationservice.locationservice.locations.dto.response.CityResponseDto;
import com.locationservice.locationservice.locations.mapper.CityMapper;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CityServiceImpl implements CityService{
    Map<String, UUID> cityMap=new ConcurrentHashMap<>();
    Map<String, List<CityResponseDto>>stateMap=new ConcurrentHashMap<>();
    @Autowired
    private RedisService redisService;

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final ModelMapper mapper;

    public CityServiceImpl(CountryRepository countryRepository, CityRepository cityRepository, StateRepository stateRepository, ModelMapper mapper) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.mapper = mapper;
    }
    @PostConstruct
    public void initialize() {
       List<City>cityList= cityRepository.findAll();
       for(City city:cityList){
//           String cityKey="cityId:"+city.getId();
//           Object cityCache=redisService.get(cityKey);
//           if(cityCache==null){
//               redisService.set(cityKey,city,1, TimeUnit.HOURS);
//           }
           cityMap.put(city.getCityName(),city.getId());
       }

        System.out.println("CITY CACHES ADDED ");
    }

    @Override
    public CityNameResponseDto createCity(CityRequestDto dto) {
        UUID cityId;
        CityNameResponseDto cityNameResponseDto=new CityNameResponseDto();
        for (CityDetailRequestDto cityDetailRequestDto : dto.getCityDetailRequestDtoList()) {
            String cityKey = dto.getCountryName().toUpperCase() + ":" + cityDetailRequestDto.getCityName() + ":" + dto.getStateName().toUpperCase();
            cityId = cityMap.get(cityKey);
            if (cityId != null) {
                City city = cityRepository.getReferenceById(cityId);

                return mapper.map(city, CityNameResponseDto.class);
            }


            States state = stateRepository.findByCountryIdAndStateName(dto.getCountryId(), dto.getStateName()).orElseThrow(
                    () -> new RuntimeException("State not found".toUpperCase()));

            City city = new City();
            city.setCityName(cityDetailRequestDto.getCityName().toUpperCase());
            city.setCityImage(cityDetailRequestDto.getCityImage());
            city.setStates(state);


            city = cityRepository.save(city);
            List<CityResponseDto>responseDtoList=new ArrayList<>();
            for(City cities:List.of(city)){
                responseDtoList.add(CityMapper.fromCityByState(cities));
            }

            stateMap.put(dto.getStateName(),responseDtoList);
            cityMap.put(cityKey, city.getId());

            cityNameResponseDto=mapper.map(city, CityNameResponseDto.class);

            redisService.set("city:"+cityId,cityNameResponseDto ,1,TimeUnit.HOURS);

        }
        return cityNameResponseDto;
    }



    @Override
    public List<CityNameResponseDto> getAll() {
        String key="allcities";
        Object cache=redisService.get(key);
        if(cache!=null){
            return (List<CityNameResponseDto>) cache;
        }
            List<City>cityList=cityRepository.findAll();
            List<CityNameResponseDto>responseDtos=new ArrayList<>();
            for(City city:cityList){
                responseDtos.add(mapper.map(city,CityNameResponseDto.class));
            }
            redisService.set(key,responseDtos,30,TimeUnit.HOURS);
            return responseDtos;
        }

    @Override
    public List<CityResponseDto> findByStateName(String stateName) {
        List<CityResponseDto> responseDtos=new ArrayList<>();
        if(stateMap.containsKey(stateName.toUpperCase())){
            System.out.println(" CACHE HIT for: " + stateName.toUpperCase());
            return stateMap.get(stateName.toUpperCase());
        }
        Optional<States>statesOptional=stateRepository.findByStateName(stateName.toUpperCase());
        System.out.println("DB CALL");
        if(statesOptional.isEmpty()){
            throw new RuntimeException("invalid state entered "+stateName);
        }
        List<City>cityList=cityRepository.findAllCitiesByState(stateName.toUpperCase());
        if(cityList.isEmpty()){
            throw new RuntimeException("WE DO NOT HAVE BRANCH IN THIS STATE "+stateName);
        }
        List<CityResponseDto>cityResponseDtoList=new ArrayList<>();
        for(City city:cityList){
            cityResponseDtoList.add(CityMapper.fromCityByState(city));
        }
        stateMap.put(stateName,cityResponseDtoList);
        return cityResponseDtoList;
    }

    @Override
    public CityResponseDto getByCityName(String cityName) {
        Optional<City>cityOptional=cityRepository.findByCityName(cityName);
        if(cityOptional.isEmpty()){
            throw new RuntimeException("CITY NAME IS INVALID "+cityName);
        }
        return CityMapper.fromCityByState(cityOptional.get());
    }

}
