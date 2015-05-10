package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class VehicleControllerTest extends Specification {

    MockMvc mockMvc

    VehicleController vehicleController

    def setup() {
        vehicleController = new VehicleController()
        vehicleController.vehicleRepository = Mock(VehicleRepository)
        vehicleController.userRepository = Mock(UserRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build()
    }

    def "Test get action"() {
        setup:
        Vehicle vehicle = new Vehicle(id: 1)
        vehicleController.vehicleRepository.findOne(1) >> vehicle

        when:
        def response = mockMvc.perform(MRB.get("/vehicle/get/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test get action with unknown vehicle"() {
        setup:
        vehicleController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("/vehicle/get/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test list by user"() {
        setup:
        User fakeUser = new User(id: 1, username: "fakeUser")
        vehicleController.userRepository.findOne(1) >> fakeUser
        def vehicles = []
        (0..5).each {
            vehicles << new Vehicle(id: it, registerNumber: it, user: fakeUser)
        }
        vehicleController.vehicleRepository.findAllByUserId(1, _) >> new PageImpl(vehicles)

        when:
        def response = mockMvc.perform(MRB.get("/vehicle/list/1/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("totalElements").value(6))
    }

    def "Test list by user unknown"() {
        setup:
        vehicleController.userRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("/vehicle/list/1/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }
}
