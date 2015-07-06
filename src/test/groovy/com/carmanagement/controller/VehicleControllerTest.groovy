package com.carmanagement.controller

import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.interfaces.UserService
import groovy.json.JsonBuilder
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class VehicleControllerTest extends AbstractControllerTest {

    VehicleController vehicleController

    def setup() {
        vehicleController = new VehicleController()
        vehicleController.vehicleRepository = Mock(VehicleRepository)
        vehicleController.userService = Mock(UserService)
        setupMockMvc(vehicleController)
    }

    def "Test get action"() {
        setup:
        Vehicle vehicle = new Vehicle(id: 1)
        vehicleController.vehicleRepository.findOne(1) >> vehicle
        vehicleController.userService.checkUserVehicle(_, _) >> true

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test get action with unknown vehicle"() {
        setup:
        vehicleController.vehicleRepository.findOne(1) >> null
        vehicleController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test list by user"() {
        setup:
        User fakeUser = new User(id: 1, name: "fakeUser")
        def vehicles = []
        (0..5).each {
            vehicles << new Vehicle(id: it, registerNumber: it, user: fakeUser)
        }
        vehicleController.vehicleRepository.findAllByUserName(_, _) >> new PageImpl(vehicles)

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/?page=0"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("totalElements").value(6))
    }

    def "Test save"() {
        setup:
        vehicleController.vehicleRepository.save(_) >> new Vehicle(id: 1)
        def json = new JsonBuilder(new Vehicle(registerNumber: 1245)).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("/vehicles").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test save with no vehicle"() {
        when:
        def response = mockMvc.perform(MRB.post("/vehicles").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string(""))
    }

    def "Test delete"() {
        setup:
        vehicleController.vehicleRepository.findOne(_) >> new Vehicle(id: 1)

        when:
        def response = mockMvc.perform(MRB.delete("/vehicles").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string("Deleted : 1"))
    }

    def "Test delete with vehicle not found"() {
        setup:
        vehicleController.vehicleRepository.findOne(_) >> null

        when:
        def response = mockMvc.perform(MRB.delete("/vehicles").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test find all"() {
        setup:
        vehicleController.vehicleRepository.findAll() >> [new Vehicle(id: 1, registerNumber: "register1"), new Vehicle(id: 2, registerNumber: "register2")]

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/list"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("\$").isArray())
    }
}
