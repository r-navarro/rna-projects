package com.carmanagement.services.interfaces

import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface VehiclesService {

    Vehicle save(Vehicle vehicle, User user)

    Vehicle update(Vehicle vehicle)

    Page<Vehicle> getVehicles(Pageable pageable, String name)

    Vehicle get(Long id, String name)

    void delete(Long id) throws TechnicalException

    List<Vehicle> findAll()
}