package com.carmanagement.controller

import com.carmanagement.dto.VehicleDTO
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
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = "/vehicles")
@Secured("ROLE_USER")
@Slf4j
class VehicleController {

    static final int PAGE_SIZE = 2

    @Autowired
    private VehicleRepository vehicleRepository

    @Autowired
    private VehiclesService vehiclesService

    @Autowired
    private UserService userService

    @RequestMapping(method = RequestMethod.POST)
    def VehicleDTO save(@RequestBody VehicleDTO vehicle) {
        def auth = SecurityContextHolder.getContext().authentication
        def user = userService.findByName(auth.name)
        return vehiclesService.save(vehicle, user.id)
    }

    @RequestMapping(method = RequestMethod.DELETE)
    def String delete(@RequestBody Long id) {
        def vehicle = vehicleRepository.findOne(id)
        if (vehicle) {
            vehicleRepository.delete(vehicle)
            return "Deleted : ${id}"
        }
        throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    def Iterable<Vehicle> findAll() {
        return vehicleRepository.findAll()
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    def Vehicle get(@PathVariable Long id) {
        def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            def auth = SecurityContextHolder.getContext().authentication
            if (userService.checkUserVehicle(auth.name, vehicle))
                return vehicle
        }
        throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
    }

    @RequestMapping(method = RequestMethod.GET)
    def Page<Vehicle> getVehicle(@PageableDefault(size = VehicleController.PAGE_SIZE, page = 0) Pageable pageable) {
        def auth = SecurityContextHolder.getContext().authentication
        def result = vehicleRepository.findAllByUserName(auth.name, pageable)
        return result
    }
}
