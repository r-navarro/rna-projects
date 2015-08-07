package com.carmanagement.services

import com.carmanagement.dto.FullTankDTO
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.impls.FullTanksServiceImpl
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class FullTanksServiceTest extends Specification {

    FullTanksServiceImpl fullTanksService

    FullTankDTO fullTankDTO = new FullTankDTO(id: 1, distance: 1, quantity: 1, cost: 1, date: new Date())

    Vehicle vehicle = new Vehicle(id: 1)

    FullTank fullTank = new FullTank(id: 1, distance: 1, quantity: 1, cost: 1, date: new Date(), vehicle: vehicle)

    def setup() {
        fullTanksService = new FullTanksServiceImpl()
        fullTanksService.fullTankRepository = Stub(FullTankRepository)
        fullTanksService.vehicleRepository = Stub(VehicleRepository)
    }

    def "test get fullTank"() {
        setup:
        fullTanksService.fullTankRepository.findOne(_) >> fullTank

        when:
        def result = fullTanksService.get(1, 1)

        then:
        result.id == fullTankDTO.id
        result.distance == fullTankDTO.distance
        result.quantity == fullTankDTO.quantity
        result.cost == fullTankDTO.cost
        result.date == fullTank.date
    }

    def "test get fullTank with wrong fullTank id"() {
        setup:
        fullTanksService.fullTankRepository.findOne(_) >> null

        when:
        def result = fullTanksService.get(1, 1)

        then:
        !result
    }

    def "test get fullTank with wrong vehicle id"() {
        setup:
        fullTanksService.fullTankRepository.findOne(_) >> fullTank

        when:
        def result = fullTanksService.get(2, 1)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: 2).message
    }

    def "get fullTanks paginate"() {
        setup:
        def fullTanksList = []
        5.times {
            fullTanksList << new FullTank(id: it)
        }
        fullTanksService.vehicleRepository.findOne(_) >> vehicle
        fullTanksService.fullTankRepository.findByVehicleId(_, _) >> new PageImpl<FullTank>(fullTanksList)

        when:
        def result = fullTanksService.getFullTanks(new PageRequest(1, 1), 1)

        then:
        result.totalElements == 5
        result.content.size() == fullTanksList.size()

    }

    def "get fullTanks paginate with vehicle not found"() {
        setup:
        fullTanksService.vehicleRepository.findOne(_) >> null

        when:
        def result = fullTanksService.getFullTanks(new PageRequest(1, 1), 1)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: 1).message
    }
}