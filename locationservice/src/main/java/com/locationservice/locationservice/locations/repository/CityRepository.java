package com.locationservice.locationservice.locations.repository;


import com.locationservice.locationservice.locations.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

    @Query("SELECT c FROM City c WHERE c.states.stateName = :stateName")
    List<City> findAllCitiesByState(@Param("stateName") String stateName);
    Optional<City>findByCityName(String cityName);
    List<City> findByCityNameIn(List<String> cityNames);
    List<City> findByStatesId(UUID stateId);
    Optional<City> findByStatesIdAndCityName(UUID stateId, String cityName);
}
