package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.VehicleRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@ContextConfiguration(classes=PersistenceTestConfig.class)
@ActiveProfiles("test")
class VehicleControllerTest extends Specification{

    MockMvc mockMvc
    VehicleController vehicleController

    def setup(){
        vehicleController = new VehicleController()
        vehicleController.vehicleRepository = Mock(VehicleRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(new VehicleController()).build()
    }

    def "Test get action"(){
        when:
        vehicleController.vehicleRepository.findOne(1) >> new Vehicle(id:1)
        def response = mockMvc.perform(MRB.get("/vehicle/get/1"))

        then :
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string("index"))
    }
}
