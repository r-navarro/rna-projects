package com.carmanagement.controller

import com.carmanagement.dto.VehicleDTO
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    private VehiclesService vehiclesService

    @Autowired
    private UserService userService

    @RequestMapping(method = RequestMethod.POST)
    def ResponseEntity<VehicleDTO> save(@RequestBody VehicleDTO vehicle) {
        if (vehicle) {
            def auth = SecurityContextHolder.getContext().authentication
            def user = userService.findByName(auth.name)
            def vehicleSaved = vehiclesService.save(vehicle.toVehicle(), user)
            return new ResponseEntity(new VehicleDTO(vehicleSaved), HttpStatus.CREATED)
        }
        throw new TechnicalException(errorCode: ErrorCode.VEHICLE_WRONG_FORMAT)
    }

    @RequestMapping(value = '/{id}', method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    def void delete(@PathVariable Long id) {
        vehiclesService.delete(id)
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    def List<VehicleDTO> findAll() {
        return vehiclesService.findAll()
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    def VehicleDTO get(@PathVariable Long id) {
        def auth = SecurityContextHolder.getContext().authentication
        def vehicle = vehiclesService.get(id, auth.name)
        if (!vehicle) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: id)
        }
        return new VehicleDTO(vehicle)
    }

    @RequestMapping(method = RequestMethod.GET)
    def Page<VehicleDTO> getVehicles(@PageableDefault(size = VehicleController.PAGE_SIZE, page = 0) Pageable pageable) {
        def auth = SecurityContextHolder.getContext().authentication
        return vehiclesService.getVehicles(pageable, auth.name)
    }
}
