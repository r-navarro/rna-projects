package com.carmanagement.repositories

import org.springframework.data.repository.CrudRepository

import com.carmanagement.entities.Vehicle

interface VehicleRepository extends CrudRepository<Vehicle, Long> {

	Vehicle findByRegisterNumber(String registerNumber)
}
