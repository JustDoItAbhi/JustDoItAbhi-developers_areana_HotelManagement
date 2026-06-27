package com.locationservice.locationservice.locations.repository;


import com.locationservice.locationservice.locations.model.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface StateRepository extends JpaRepository<States, UUID> {
Optional<States>findByStateName(String name);

    Optional<States> findByCountryIdAndStateName(UUID countryId, String stateName);
    @Query(" SELECT s FROM States s JOIN FETCH s.country ")
    List<States> findAllWithCountry();
    @Query("""
       SELECT DISTINCT s
       FROM States s
       LEFT JOIN FETCH s.cityList
       WHERE s.country.countryName = :countryName
       """)
    List<States> findAllStatesWithCitiesByCountry(
            @Param("countryName") String countryName);
}
