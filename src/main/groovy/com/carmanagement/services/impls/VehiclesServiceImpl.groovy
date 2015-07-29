package com.carmanagement.services.impls

import com.carmanagement.dto.VehicleDTO
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.VehiclesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VehiclesServiceImpl implements VehiclesService {

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserRepository userRepository

    @Override
    VehicleDTO save(VehicleDTO vehicleDTO, Long userId) {
        def vehicle = vehicleDTO.toVehicle()

        def user = userRepository.findOne(userId)
        vehicle.user = user
        vehicle = vehicleRepository.save(vehicle)

        return new VehicleDTO(vehicle)
    }
}
