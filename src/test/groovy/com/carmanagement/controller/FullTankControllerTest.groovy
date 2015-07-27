package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import groovy.json.JsonBuilder
import groovy.time.TimeCategory
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class FullTankControllerTest extends AbstractControllerTest {

    FullTankController fullTankController

    String baseUrl = "/vehicles/1/fullTanks"

    Vehicle vehicle = new Vehicle(id: 1)

    FullTank fullTank = new FullTank(id: 1, vehicle: vehicle, cost: 1, mileage: 1, date: new Date(), quantity: 1)


    def setup() {
        fullTankController = new FullTankController()
        fullTankController.fullTankRepository = Mock(FullTankRepository)
        fullTankController.vehicleRepository = Mock(VehicleRepository)
        setupMockMvc(fullTankController)
    }


    def "Test get action"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.findOne(1) >> fullTank

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test get action with unknown vehicle"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("request").value("$baseUrl/1".toString()))
    }

    def "Test get action with unknown fullTank"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test list"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        def fullTanks = []
        (0..5).each {
            fullTanks << new FullTank(id: it, vehicle: vehicle, cost: 1, mileage: 1, date: new Date(), quantity: 1)
        }
        fullTankController.fullTankRepository.findByVehicleId(1, _) >> new PageImpl(fullTanks)

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/?page=0"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("totalElements").value(6))
    }

    def "Test list by vehicle unknown"() {
        setup:
        fullTankController.vehicleRepository.findOne(_) >> null

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/?page=0"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test save"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.save(_) >> fullTank
        def json = new JsonBuilder(new FullTank(vehicle: vehicle, cost: 1, mileage: 1, date: new Date(), quantity: 1)).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isCreated())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test save with vehicle not found"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test save with no full tank"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT).getMessage()))
    }

    def "Test update"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.save(_) >> fullTank
        def json = new JsonBuilder(fullTank).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$fullTank.id").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isCreated())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test update with vehicle not found"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$fullTank.id").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test update with no full tank"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$fullTank.id").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT).getMessage()))
    }

    def "Test cost stats"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        def date1 = new Date()
        def date2 = new Date()
        use(TimeCategory) {
            date2 = date1 + 1.month
        }
        fullTankController.fullTankRepository.findAllByVehicleId(1) >> [new FullTank(cost: 2, date: date2), new FullTank(cost: 1, date: date1)]

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/costStats"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath('$[0].[0]').value(date1.format('dd/MM/yyyy')))
        response.andExpect(MRM.jsonPath('$[0].[1]').value(1.0d))
        response.andExpect(MRM.jsonPath('$[1].[0]').value(date2.format('dd/MM/yyyy')))
        response.andExpect(MRM.jsonPath('$[1].[1]').value(2.0d))
    }

    def "Test distance stats"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        def date1 = new Date()
        def date2 = new Date()
        use(TimeCategory) {
            date2 = date1 + 1.month
        }
        fullTankController.fullTankRepository.findAllByVehicleId(1) >> [new FullTank(mileage: 125.62, date: date2), new FullTank(mileage: 564.23, date: date1)]

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/distanceStats"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath('$[0].[0]').value(date1.format('dd/MM/yyyy')))
        response.andExpect(MRM.jsonPath('$[0].[1]').value(564.23d))
        response.andExpect(MRM.jsonPath('$[1].[0]').value(date2.format('dd/MM/yyyy')))
        response.andExpect(MRM.jsonPath('$[1].[1]').value(125.62d))
    }

    def "Test delete"() {
        setup:
        fullTankController.fullTankRepository.findOne(fullTank.id) >> fullTank

        when:
        def response = mockMvc.perform(MRB.delete("$baseUrl/$fullTank.id"))

        then:
        response.andExpect(MRM.status().isNoContent())
    }

    def "Test delete with no fullTank"() {

        when:
        def response = mockMvc.perform(MRB.delete("$baseUrl/$fullTank.id"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTank.id).getMessage()))
    }

    def "Test delete with bad vehicle"() {
        setup:
        fullTankController.fullTankRepository.findOne(fullTank.id) >> fullTank

        when:
        def response = mockMvc.perform(MRB.delete("/vehicles/2/fullTanks/$fullTank.id"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH, errorParameter: 2).getMessage()))
    }
}
