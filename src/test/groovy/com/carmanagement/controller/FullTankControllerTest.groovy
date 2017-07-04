package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class FullTankControllerTest extends AbstractControllerTest {

    def baseUrl = "/vehicles/##/fullTanks"

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    FullTankRepository fullTankRepository

    Vehicle vehicle = new Vehicle(registerNumber: 'Integration test vehicle')

    FullTank fullTank = new FullTank(vehicle: vehicle, cost: 1, distance: 1, date: new Date(), quantity: 1)


    def init() {
        vehicle = vehicleRepository.save(vehicle)
        fullTank.vehicle = vehicle
        fullTank = fullTankRepository.save(fullTank)
    }

    def "Test get action"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl.replaceAll('##', vehicle.id), totoHttpBasic()))
            def resultJson = getResponseAsJsonObject(response)
            def content = resultJson.content as List

        then:
            response.andExpect(MRM.status().isOk())
            response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
            content.size() == 1
    }
}
