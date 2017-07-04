package com.carmanagement.services.interfaces

import com.carmanagement.entities.Maintenance
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MaintenancesService {

    Maintenance getByVehicleIdAndId(String vehicleId, String maintenanceId) throws TechnicalException

    Page<Maintenance> getMaintenances(Pageable pageable, String vehicleId) throws TechnicalException

    Maintenance save(Maintenance maintenance, String vehicleId) throws TechnicalException

    void delete(String vehicleId, String maintenanceId) throws TechnicalException


}