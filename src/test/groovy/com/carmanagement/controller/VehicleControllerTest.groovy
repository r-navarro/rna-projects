package com.carmanagement.controller

import com.carmanagement.dto.VehicleDTO
import groovy.json.JsonBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class VehicleControllerTest extends AbstractControllerTest {

    def baseUrl = '/vehicles'

    def vehicleDTO = new VehicleDTO(registrationNumber: 'Integration test vehicle', price: 1, kilometers: 1, type: "test")

    def "test get all vehicle"() {
        when:
            def response = mockMvc.perform(MRB.get("$baseUrl/list").with(adminHttpBasic()))
        then:
            response.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "test create a vehicle"() {
        when:
            def response = mockMvc.perform(getPostQuery(baseUrl, new JsonBuilder(vehicleDTO).toString(), totoHttpBasic()))
        then:
            response.andExpect(MRM.status().isCreated())
    }

}
