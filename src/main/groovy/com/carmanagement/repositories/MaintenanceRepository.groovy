package com.carmanagement.repositories

import com.carmanagement.entities.Maintenance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface MaintenanceRepository extends PagingAndSortingRepository<Maintenance, Long> {

    Page<Maintenance> findByVehicleId(Long vehicleId, Pageable pageable)

    List<Maintenance> findAllByVehicleId(Long vehicleId)
}