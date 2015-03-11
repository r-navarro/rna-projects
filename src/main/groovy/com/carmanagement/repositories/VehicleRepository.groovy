package com.carmanagement.repositories

import org.springframework.data.repository.PagingAndSortingRepository

import com.carmanagement.entities.Vehicle

interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Long> {

	Vehicle findByRegisterNumber(String registerNumber)
}
