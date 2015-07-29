package com.carmanagement.services.interfaces

import com.carmanagement.dto.VehicleDTO

interface VehiclesService {

    VehicleDTO save(VehicleDTO vehicleDTO, Long userId)
}