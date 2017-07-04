package com.carmanagement.services.interfaces

import com.carmanagement.dto.StatAverageDTO
import com.carmanagement.dto.StatDTO
import com.carmanagement.entities.FullTank
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FullTanksService {

    FullTank getByVehicleIdAndId(String vehicleId, String fullTankId) throws TechnicalException

    Page<FullTank> getFullTanks(Pageable pageable, String vehicleId) throws TechnicalException

    FullTank save(FullTank fullTank, String vehicleId) throws TechnicalException

    FullTank update(FullTank fullTank, String vehicleId) throws TechnicalException

    void delete(String vehicleId, String fullTankId) throws TechnicalException

    List<StatDTO> getCostStats(String vehicleId)

    List<StatDTO> getDistanceStats(String vehicleId)

    List<StatAverageDTO> getAverageStats(String vehicleId)
}