package com.carmanagement.controller

import com.carmanagement.dto.UserDTO
import com.carmanagement.dto.VehicleDTO
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import groovy.json.JsonBuilder
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class VehicleControllerTest extends AbstractControllerTest {

    VehicleController vehicleController

    def vehicle = new Vehicle(id: 1)

    def vehicleDTO = new VehicleDTO(id: 1, registerNumber: "test", kilometers: 1, price: 1, type: "test")

    def user = new User(id: 1, name: "fakeUser")

    def userDTO = new UserDTO(id: 1, name: "fakeUser")

    def setup() {
        vehicleController = new VehicleController()
        vehicleController.vehiclesService = Mock(VehiclesService)
        vehicleController.userService = Mock(UserService)
        setupMockMvc(vehicleController)
    }

    def "Test get action"() {
        setup:
        vehicleController.vehiclesService.get(_, _) >> vehicleDTO

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test get action with unknown vehicle"() {
        setup:
        vehicleController.vehiclesService.get(_, _) >> null

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test list by user"() {
        setup:
        def vehicles = []
        (0..5).each {
            vehicles << new VehicleDTO(id: it, registerNumber: it)
        }
        vehicleController.vehiclesService.getVehicles(_, _) >> new PageImpl(vehicles)

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/?page=0"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("totalElements").value(6))
    }

    def "Test save"() {
        setup:
        vehicleController.vehiclesService.save(_, _) >> vehicleDTO
        vehicleController.userService.findByName(_) >> userDTO
        def json = new JsonBuilder(vehicleDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("/vehicles").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isCreated())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test save with no vehicle"() {
        when:
        def response = mockMvc.perform(MRB.post("/vehicles").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.VEHICLE_WRONG_FORMAT).getMessage()))
    }

    def "Test delete"() {
        when:
        def response = mockMvc.perform(MRB.delete("/vehicles").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isNoContent())
    }

    def "Test delete with vehicle not found"() {
        setup:
        vehicleController.vehiclesService.delete(_) >> {
            throw new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: 1)
        }

        when:
        def response = mockMvc.perform(MRB.delete("/vehicles").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: 1).getMessage()))
    }

    def "Test find all"() {
        setup:
        vehicleController.vehiclesService.findAll() >> [new VehicleDTO(id: 1, registerNumber: "register1"), new VehicleDTO(id: 2, registerNumber: "register2")]

        when:
        def response = mockMvc.perform(MRB.get("/vehicles/list"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("\$").isArray())
    }
}
