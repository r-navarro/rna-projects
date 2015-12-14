package com.carmanagement.services.interfaces

import com.carmanagement.entities.Maintenance
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MaintenancesService {

    Maintenance getByVehicleIdAndId(Long vehicleId, Long maintenanceId) throws TechnicalException

    Page<Maintenance> getMaintenances(Pageable pageable, Long vehicleId) throws TechnicalException

    Maintenance save(Maintenance maintenance, Long vehicleId) throws TechnicalException

    void delete(Long vehicleId, Long maintenanceId) throws TechnicalException


}