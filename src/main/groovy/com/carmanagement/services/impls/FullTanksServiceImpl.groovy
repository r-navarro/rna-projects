package com.carmanagement.services.impls

import com.carmanagement.dto.FullTankDTO
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.FullTanksService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FullTanksServiceImpl implements FullTanksService {

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @Override
    FullTankDTO get(Long vehicleId, Long fullTankId) throws TechnicalException {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (fullTank) {
            if (fullTank.vehicle.id != vehicleId) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return new FullTankDTO(fullTank)
        }
        return null
    }

    @Override
    Page<FullTankDTO> getFullTanks(Pageable pageable, Long vehicleId) throws TechnicalException {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        def fullTankPage = fullTankRepository.findByVehicleId(vehicleId, pageable)

        def fullTankDTOs = []

        fullTankPage.content.each {
            fullTankDTOs << new FullTankDTO(it)
        }

        return new PageImpl<FullTankDTO>(fullTankDTOs, pageable, fullTankPage.totalElements)
    }

    @Override
    FullTankDTO save(FullTankDTO fullTankDTO, Long vehicleId) throws TechnicalException {
        if (fullTankDTO) {
            def vehicle = vehicleRepository.findOne(vehicleId)
            if (!vehicle) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            def fullTank = fullTankDTO.toFullTank()
            if(fullTank.id){
                fullTank = updateFullTank(fullTank)
            }else {
                fullTank = addFullTank(vehicle, fullTank)
            }
            return new FullTankDTO(fullTank)
        }
        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT)
    }

    private FullTank addFullTank(Vehicle vehicle, FullTank fullTank){
        fullTank.vehicle = vehicle
        vehicle.kilometers += fullTank.quantity
        vehicle.actions << fullTank

        return fullTankRepository.save(fullTank)
    }

    private FullTank updateFullTank(FullTank fullTank) throws TechnicalException {
        def oldFullTank = fullTankRepository.findOne(fullTank.id)
        if (oldFullTank) {
            def vehicle = oldFullTank.vehicle
            vehicle.kilometers -= oldFullTank.distance
            vehicle.kilometers += fullTank.distance
            vehicle.actions.remove(oldFullTank)
            vehicle.actions << fullTank
            fullTank.vehicle = vehicle

            return fullTankRepository.save(fullTank)
        }
        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTank.id)
    }
}
