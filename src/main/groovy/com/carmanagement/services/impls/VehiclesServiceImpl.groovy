package com.carmanagement.services.impls

import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
class VehiclesServiceImpl implements VehiclesService {

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserService userService

    @Override
    @Transactional
    Vehicle save(Vehicle vehicle, User user) {
        vehicle.user = user
        vehicle = vehicleRepository.save(vehicle)

        return vehicle
    }

    @Override
    Vehicle get(Long id, String name) {
        def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            if (userService.checkUserVehicle(name, vehicle))
                return vehicle
        }
        return null
    }

    @Override
    Page<Vehicle> getVehicles(Pageable pageable, String name) {
        return vehicleRepository.findAllByUserName(name, pageable)
    }

    @Override
    @Transactional
    void delete(Long id) throws TechnicalException {
        def vehicle = vehicleRepository.findOne(id)
        if (!vehicle) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
        }
        vehicle.user.vehicles.remove(vehicle)
        vehicleRepository.delete(id)
    }

    @Override
    List<Vehicle> findAll() {
        return vehicleRepository.findAll()
    }
}
