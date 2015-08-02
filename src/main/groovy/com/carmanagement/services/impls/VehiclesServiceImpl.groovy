package com.carmanagement.services.impls

import com.carmanagement.dto.VehicleDTO
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class VehiclesServiceImpl implements VehiclesService {

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @Override
    VehicleDTO save(VehicleDTO vehicleDTO, Long userId) {
        def vehicle = vehicleDTO.toVehicle()

        def user = userRepository.findOne(userId)
        vehicle.user = user
        vehicle = vehicleRepository.save(vehicle)

        return new VehicleDTO(vehicle)
    }

    @Override
    VehicleDTO get(Long id, String name) {
        def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            if (userService.checkUserVehicle(name, vehicle))
                return new VehicleDTO(vehicle)
        }
        return null
    }

    @Override
    Page<VehicleDTO> getVehicles(Pageable pageable, String name) {
        def entitiesPage = vehicleRepository.findAllByUserName(name, pageable)

        def dtos = []

        entitiesPage.content.each {
            dtos << new VehicleDTO(it)
        }

        def result = new PageImpl<VehicleDTO>(dtos, pageable, entitiesPage.totalElements)

        return result
    }

    @Override
    void delete(Long id) throws TechnicalException {
        def vehicle = vehicleRepository.findOne(id)
        if (vehicle) {
            vehicleRepository.delete(vehicle)
        }
        throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
    }

    @Override
    List<VehicleDTO> findAll() {
        def result = []
        vehicleRepository.findAll().each {
            result << new VehicleDTO(it)
        }
        return result
    }
}
