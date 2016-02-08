package com.carmanagement.services.impls

import com.carmanagement.entities.Maintenance
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.MaintenanceRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.MaintenancesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MaintenancesServiceImpl implements MaintenancesService {

    @Autowired
    MaintenanceRepository maintenanceRepository

    @Autowired
    VehicleRepository vehicleRepository

    @Override
    Maintenance getByVehicleIdAndId(Long vehicleId, Long maintenanceId) throws TechnicalException {
        def maintenance = getMaintenance(maintenanceId)
        if (maintenance) {
            if (maintenance.vehicle.id != vehicleId) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return maintenance
        }
        throw new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: maintenanceId)
    }

    @Override
    Page<Maintenance> getMaintenances(Pageable pageable, Long vehicleId) throws TechnicalException {
        if (!getVehicle(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        return maintenanceRepository.findByVehicleId(vehicleId, pageable)
    }

    @Override
    @Transactional
    Maintenance save(Maintenance maintenance, Long vehicleId) throws TechnicalException {
        if (maintenance) {
            def vehicle = getVehicle(vehicleId)
            if (!vehicle) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            if (maintenance.id) {
                vehicle >> maintenanceRepository.findOne(maintenance.id)
            }
            vehicle << maintenance
            return maintenanceRepository.save(maintenance)
        }
        throw new TechnicalException(errorCode: ErrorCode.MAINTENANCE_WRONG_FORMAT)
    }

    @Override
    void delete(Long vehicleId, Long maintenanceId) throws TechnicalException {
        def maintenance = getMaintenance(maintenanceId)
        if (!maintenance) {
            throw new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: maintenanceId)
        }
        if (maintenance.vehicle.id != vehicleId) {
            throw new TechnicalException(errorCode: ErrorCode.MAINTENANCE_VEHICLE_NOT_MATCH, errorParameter: vehicleId)
        }
        maintenanceRepository.delete(maintenanceId)
    }

    private Vehicle getVehicle(Long id) {
        if (!id) {
            return null
        }
        return vehicleRepository.findOne(id)
    }

    private Maintenance getMaintenance(Long id) {
        if (!id) {
            return null
        }
        return maintenanceRepository.findOne(id)
    }
}





