package com.carmanagement.repositories

import com.carmanagement.entities.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Vehicle findByRegisterNumber(String registerNumber)

    Page<Vehicle> findAllByUserId(Long userId, Pageable pageable)

    Page<Vehicle> findAllByUserName(String username, Pageable pageable)

}
