package com.carmanagement.controller

import com.carmanagement.dto.FullTankDTO
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.FullTanksService
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

    @Autowired
    FullTanksService fullTanksService

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.GET)
    def FullTankDTO get(@PathVariable("vehicleId") Long vehicleId, @PathVariable("fullTankId") Long fullTankId) {
        def fullTank = fullTanksService.get(vehicleId, fullTankId)

        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
        }
        return fullTank
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.GET)
    def Page<FullTankDTO> findFullTankByVehiclePaginate(
            @PathVariable("vehicleId") Long vehicleId,
            @PageableDefault(size = FullTankController.PAGE_SIZE, page = 0) Pageable pageable) {

        return fullTanksService.getFullTanks(pageable, vehicleId)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.POST)
    def ResponseEntity<FullTankDTO> create(
            @PathVariable("vehicleId") Long vehicleId, @RequestBody FullTankDTO fullTank) {
        def fullTankDTO = fullTanksService.save(fullTank, vehicleId)
        return new ResponseEntity<FullTankDTO>(fullTankDTO, HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.PUT)
    def ResponseEntity<FullTankDTO> update(
            @PathVariable("vehicleId") Long vehicleId,
            @PathVariable("fullTankId") Long fullTankId, @RequestBody FullTankDTO fullTank) {
        fullTank.id = fullTankId
        def fullTankDTO = fullTanksService.save(fullTank, vehicleId)
        return new ResponseEntity<FullTankDTO>(fullTankDTO, HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    def void delete(@PathVariable("vehicleId") Long vehicleId, @PathVariable("id") Long fullTankId) {
        fullTanksService.delete(vehicleId, fullTankId)
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
        fullTanks = fullTanks.sort { it.date }.collect { [it.distance, it.date] }
        fullTanks.each {
            stats << [it[1].format('dd/MM/yyyy'), it[0]]
        }

        return stats
    }
}