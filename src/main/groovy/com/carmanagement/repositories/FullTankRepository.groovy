package com.carmanagement.repositories

import com.carmanagement.entities.FullTank
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface FullTankRepository extends PagingAndSortingRepository<FullTank, Long> {

    Page<FullTank> findByVehicleId(Long vehicleId, Pageable pageable)

    List<FullTank> findAllByVehicleId(Long vehicleId)
}