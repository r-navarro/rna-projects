package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = "/vehicles/")
@Secured("ROLE_USER")
@Slf4j
class FullTankController {

    static final int PAGE_SIZE = 5

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.GET)
    def FullTank get(@PathVariable("vehicleId") Long vehicleId, @PathVariable("fullTankId") Long fullTankId) {
        def fullTank = fullTankRepository.findOne(fullTankId)

        if (fullTank) {
            if (fullTank.vehicle.id != vehicleId) {
                throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
            }
            return fullTank
        }

        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.GET)
    def Page<FullTank> findFullTankByVehiclePaginate(
            @PathVariable("vehicleId") Long vehicleId,
            @PageableDefault(size = FullTankController.PAGE_SIZE, page = 0) Pageable pageable) {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        return fullTankRepository.findByVehicleId(vehicleId, pageable)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.POST)
    def ResponseEntity<FullTank> create(@PathVariable("vehicleId") Long vehicleId, @RequestBody FullTank fullTank) {
        def vehicle = vehicleRepository.findOne(vehicleId)
        if (!vehicle) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        fullTank.vehicle = vehicle
        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT)
        }
        return new ResponseEntity<FullTank>(fullTankRepository.save(fullTank), HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.PUT)
    def ResponseEntity<FullTank> update(
            @PathVariable("vehicleId") Long vehicleId,
            @PathVariable("fullTankId") Long fullTankId, @RequestBody FullTank fullTank) {
        def vehicle = vehicleRepository.findOne(vehicleId)
        if (!vehicle) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }
        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT)
        }
        fullTank.vehicle = vehicle
        return new ResponseEntity<FullTank>(fullTankRepository.save(fullTank), HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    def void delete(@PathVariable("vehicleId") Long vehicleId, @PathVariable("id") Long fullTankId) {
        def fullTank = fullTankRepository.findOne(fullTankId)
        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
        }
        if (fullTank.vehicle.id != vehicleId) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH, errorParameter: vehicleId)
        }

        fullTankRepository.delete(fullTankId)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/costStats", method = RequestMethod.GET)
    def List getCostStats(@PathVariable("vehicleId") Long vehicleId) {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        def fullTanks = fullTankRepository.findAllByVehicleId(vehicleId)
        def stats = []
        fullTanks = fullTanks.sort { it.date }.collect { [it.cost, it.date] }
        fullTanks.each {
            stats << [it[1].format('dd/MM/yyyy'), it[0]]
        }

        return stats
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/distanceStats", method = RequestMethod.GET)
    def List getDistanceStats(@PathVariable("vehicleId") Long vehicleId) {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        def fullTanks = fullTankRepository.findAllByVehicleId(vehicleId)
        def stats = []
        fullTanks = fullTanks.sort { it.date }.collect { [it.mileage, it.date] }
        fullTanks.each {
            stats << [it[1].format('dd/MM/yyyy'), it[0]]
        }

        return stats
    }
}