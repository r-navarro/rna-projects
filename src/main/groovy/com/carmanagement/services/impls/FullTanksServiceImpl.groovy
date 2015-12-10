package com.carmanagement.services.impls

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.FullTanksService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FullTanksServiceImpl implements FullTanksService {

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @Override
    FullTank getByVehicleIdAndId(Long vehicleId, Long fullTankId) throws TechnicalException {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (fullTank) {
            if (fullTank.vehicle.id != vehicleId) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return fullTank
        }
        return null
    }

    @Override
    Page<FullTank> getFullTanks(Pageable pageable, Long vehicleId) throws TechnicalException {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        return fullTankRepository.findByVehicleId(vehicleId, pageable)
    }

    @Override
    FullTank save(FullTank fullTank, Long vehicleId) throws TechnicalException {
        if (fullTank) {
            def vehicle = vehicleRepository.findOne(vehicleId)
            if (!vehicle) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            if(fullTank.id){
                fullTank = updateFullTank(fullTank)
            }else {
                fullTank = addFullTank(vehicle, fullTank)
            }
            return fullTank
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

    @Override
    void delete(Long vehicleId, Long fullTankId) throws TechnicalException {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
        }
        if (fullTank.vehicle.id != vehicleId) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH, errorParameter: vehicleId)
        }
        fullTank.vehicle.kilometers -= fullTank.distance
        fullTankRepository.delete(fullTank)
    }
}
