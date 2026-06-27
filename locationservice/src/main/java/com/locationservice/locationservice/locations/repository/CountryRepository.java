package com.locationservice.locationservice.locations.repository;

import com.locationservice.locationservice.locations.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    Optional<Country>findByCountryName(String name);

    Optional<Country> findByCountryNameIgnoreCase(String countryName);

    @Query("SELECT DISTINCT c FROM Country c " +
            "LEFT JOIN FETCH c.stateList s " +
            "LEFT JOIN FETCH s.cityList " +
            "WHERE c.id = :countryId")
    Optional<Country> findByIdWithStatesAndCities(@Param("countryId") UUID countryId);
}
