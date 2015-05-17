package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = "/vehicle")
class FullTankController {

    private static final int PAGE_SIZE = 5

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @RequestMapping(value = "{vehicleId}/fullTank/get/{fullTankId}", method = RequestMethod.GET)
    def FullTank get(@PathVariable("vehicleId") Integer vehicleId, @PathVariable("fullTankId") Integer fullTankId) {

        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        def fullTank = fullTankRepository.findOne(fullTankId)

        if (fullTank) {
            return fullTank
        }

        throw new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTankId)
    }

    @RequestMapping(value = "{vehicleId}/fullTank/list/{page}", method = RequestMethod.GET)
    def Page<FullTank> findFullTankByVehiclePaginate(@PathVariable("vehicleId") Integer vehicleId, @PathVariable("page") Integer page){
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        PageRequest request = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.DESC, "date")
        return fullTankRepository.findByVehicleId(vehicleId, request)
    }

    @RequestMapping(value = "{vehicleId}/fullTank/save", method = RequestMethod.POST)
    def FullTank create(@PathVariable("vehicleId") Integer vehicleId, @RequestBody FullTank fullTank) {
        if (!vehicleRepository.findOne(vehicleId)) {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicleId)
        }

        return fullTankRepository.save(fullTank)
    }
}
