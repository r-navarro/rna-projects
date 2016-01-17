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
import org.springframework.data.domain.PageImpl
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
    FullTanksService fullTanksService

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.GET)
    def FullTankDTO get(@PathVariable("vehicleId") Long vehicleId, @PathVariable("fullTankId") Long fullTankId) {
        def fullTank = fullTanksService.getByVehicleIdAndId(vehicleId, fullTankId)

        if (!fullTank) {
            throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
        }
        return new FullTankDTO(fullTank)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.GET)
    public Page<FullTankDTO> findFullTankByVehiclePaginate(
            @PathVariable("vehicleId") Long vehicleId,
            @PageableDefault(size = FullTankController.PAGE_SIZE, page = 0) Pageable pageable) {

        def fullTanksPage = fullTanksService.getFullTanks(pageable, vehicleId)
        def fullTanksDTO = fullTanksPage.content.collect { new FullTankDTO(it) }

        return new PageImpl(fullTanksDTO, pageable, fullTanksPage.totalElements)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks", method = RequestMethod.POST)
    def ResponseEntity<FullTankDTO> create(
            @PathVariable("vehicleId") Long vehicleId, @RequestBody FullTankDTO fullTank) {
        def fullTankSaved = fullTanksService.save(fullTank.toFullTank(), vehicleId)
        return new ResponseEntity<FullTankDTO>(new FullTankDTO(fullTankSaved), HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{fullTankId}", method = RequestMethod.PUT)
    def ResponseEntity<FullTankDTO> update(
            @PathVariable("vehicleId") Long vehicleId,
            @PathVariable("fullTankId") Long fullTankId, @RequestBody FullTankDTO fullTank) {
        fullTank.id = fullTankId
        def fullTankSaved = fullTanksService.save(fullTank.toFullTank(), vehicleId)
        return new ResponseEntity<FullTankDTO>(new FullTankDTO(fullTankSaved), HttpStatus.CREATED)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    def void delete(@PathVariable("vehicleId") Long vehicleId, @PathVariable("id") Long fullTankId) {
        fullTanksService.delete(vehicleId, fullTankId)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/costStats", method = RequestMethod.GET)
    def List getCostStats(@PathVariable("vehicleId") Long vehicleId) {
        return fullTanksService.getCostStats(vehicleId)
    }

    @RequestMapping(value = "{vehicleId}/fullTanks/distanceStats", method = RequestMethod.GET)
    def List getDistanceStats(@PathVariable("vehicleId") Long vehicleId) {
        return fullTanksService.getDistanceStats(vehicleId)
    }
}