package org.example.flightservice.repository;

import org.example.flightservice.model.entity.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {
    City findByCityName(String name);
    City findCitiesById(Long id);
}
