package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
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


    def setup() {
        fullTankController = new FullTankController()
        fullTankController.fullTankRepository = Mock(FullTankRepository)
        fullTankController.vehicleRepository = Mock(VehicleRepository)
        setupMockMvc(fullTankController)
    }


    def "Test get action"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        FullTank fullTank = new FullTank(id: 1, vehicle: vehicle)
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
            fullTanks << new FullTank(id: it, vehicle: vehicle)
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
        fullTankController.fullTankRepository.save(_) >> new FullTank(id: 123, vehicle: vehicle)
        def json = new JsonBuilder(new FullTank(id: 1245)).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("id").value(123))
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
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string(""))
    }

    def "Test cost stats"(){
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        def date1 = new Date()
        def date2 = new Date()
        use(TimeCategory) {
            date2 = date1 + 1.month
        }
        fullTankController.fullTankRepository.findAllByVehicleId(1) >> [new FullTank(cost: 1, date: date1), new FullTank(cost: 2, date: date2)]

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/costStats"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath('$[0].cost').value("1.0"))
        response.andExpect(MRM.jsonPath('$[0].date').value(date1.format('dd/MM/yyyy')))
        response.andExpect(MRM.jsonPath('$[1].cost').value("2.0"))
        response.andExpect(MRM.jsonPath('$[1].date').value(date2.format('dd/MM/yyyy')))
    }
}
