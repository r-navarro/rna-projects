package com.carmanagement.repositories

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import org.springframework.data.repository.PagingAndSortingRepository

interface FullTankRepository extends PagingAndSortingRepository<FullTank, Long>{

    List<FullTank> findByVehicle(Vehicle vehicle)
}