package com.carmanagement.repositories

import com.carmanagement.entities.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface VehicleRepository extends MongoRepository<Vehicle, String> {

    Vehicle findByRegistrationNumber(String registrationNumber)

    Page<Vehicle> findAllByUserId(String userId, Pageable pageable)

}
