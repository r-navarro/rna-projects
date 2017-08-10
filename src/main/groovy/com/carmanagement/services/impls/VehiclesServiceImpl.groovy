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

@Service
@Slf4j
class VehiclesServiceImpl implements VehiclesService {

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserService userService

    @Override
    Vehicle save(Vehicle vehicle, User user) {
        vehicle.user = user
        vehicle = vehicleRepository.save(vehicle)

        return vehicle
    }

    Vehicle update(Vehicle vehicle) {
        def vehicleToUpdate = vehicleRepository.findOne(vehicle.id)
        vehicleToUpdate.kilometers = vehicle.kilometers
        vehicleToUpdate.price = vehicle.price
        vehicleToUpdate.registrationNumber = vehicle.registrationNumber
        vehicleToUpdate.type = vehicle.type
        vehicle = vehicleRepository.save(vehicleToUpdate)

        return vehicle
    }

    @Override
    Vehicle get(String id, String name) {
        def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            if (userService.checkUserVehicle(name, vehicle))
                return vehicle
        }
        return null
    }

    @Override
    Page<Vehicle> getVehicles(Pageable pageable, String name) {
        def user = userService.findByName(name)
        if (user) {
            return vehicleRepository.findAllByUserId(user.id, pageable)
        }

        throw new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: name)
    }

    @Override
    void delete(String id) throws TechnicalException {
        def vehicle = vehicleRepository.findOne(id)
        if (!vehicle) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
        }
        vehicleRepository.delete(id)
    }

    @Override
    List<Vehicle> findAll() {
        return vehicleRepository.findAll()
    }

}
