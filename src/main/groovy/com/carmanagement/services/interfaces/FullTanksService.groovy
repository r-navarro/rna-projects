package com.carmanagement.services.interfaces

import com.carmanagement.dto.StatAverageDTO
import com.carmanagement.dto.StatDTO
import com.carmanagement.entities.FullTank
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FullTanksService {

    FullTank getByVehicleIdAndId(Long vehicleId, Long fullTankId) throws TechnicalException

    Page<FullTank> getFullTanks(Pageable pageable, Long vehicleId) throws TechnicalException

    FullTank save(FullTank fullTank, Long vehicleId) throws TechnicalException

    void delete(Long vehicleId, Long fullTankId) throws TechnicalException

    List<StatDTO> getCostStats(Long vehicleId)

    List<StatDTO> getDistanceStats(Long vehicleId)

    List<StatAverageDTO> getAverageStats(Long vehicleId)
}