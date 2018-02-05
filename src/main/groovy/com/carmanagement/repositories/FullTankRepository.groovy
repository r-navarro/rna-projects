package com.carmanagement.repositories

import com.carmanagement.entities.FullTank
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface FullTankRepository extends PagingAndSortingRepository<FullTank, String> {

    Page<FullTank> findByVehicleId(String vehicleId, Pageable pageable)

    List<FullTank> findAllByVehicleId(String vehicleId)

    List<FullTank> findAllByVehicleIdOrderByDateAsc(String vehicleId)

    List<FullTank> findAllByVehicleIdOrderByDateDesc(String vehicleId)
}