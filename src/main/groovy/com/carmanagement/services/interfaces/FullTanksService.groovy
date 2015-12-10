package com.carmanagement.services.interfaces

import com.carmanagement.entities.FullTank
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FullTanksService {

    FullTank getByVehicleIdAndId(Long vehicleId, Long fullTankId) throws TechnicalException

    Page<FullTank> getFullTanks(Pageable pageable, Long vehicleId) throws TechnicalException

    FullTank save(FullTank fullTank, Long vehicleId) throws TechnicalException

    void delete(Long vehicleId, Long fullTankId) throws TechnicalException
}