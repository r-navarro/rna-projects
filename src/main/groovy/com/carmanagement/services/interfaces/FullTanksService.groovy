package com.carmanagement.services.interfaces

import com.carmanagement.dto.FullTankDTO
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FullTanksService {

    FullTankDTO get(Long vehicleId, Long fullTankId) throws TechnicalException

    Page<FullTankDTO> getFullTanks(Pageable pageable, Long vehicleId) throws TechnicalException

    FullTankDTO save(FullTankDTO fullTankDTO, Long vehicleId) throws TechnicalException
}