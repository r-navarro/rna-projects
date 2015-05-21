package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.FullTankRepository
import com.carmanagement.repositories.VehicleRepository
import groovy.json.JsonBuilder
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod

import java.lang.reflect.Method

class FullTankControllerTest extends AbstractControllerTest {

    FullTankController fullTankController

    String baseUrl = "/vehicle/1/fullTank"

    Vehicle vehicle = new Vehicle(id: 1)


    def setup() {
        fullTankController = new FullTankController()
        fullTankController.fullTankRepository = Mock(FullTankRepository)
        fullTankController.vehicleRepository = Mock(VehicleRepository)
        setupMockMvc(fullTankController)
    }

    private static ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler).resolveMethod(exception)
                return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method)
            }
        }
        exceptionResolver.afterPropertiesSet()
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter())
        return exceptionResolver
    }


    def "Test get action"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        FullTank fullTank = new FullTank(id: 1, vehicle: vehicle)
        fullTankController.fullTankRepository.findOne(1) >> fullTank

        when:
        def response = mockMvc.perform(MRB.get("${baseUrl}/get/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test get action with unknown vehicle"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("${baseUrl}/get/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("request").value("${baseUrl}/get/1".toString()))
    }

    def "Test get action with unknown fullTank"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.get("${baseUrl}/get/1"))

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
        def response = mockMvc.perform(MRB.get("${baseUrl}/list/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("totalElements").value(6))
    }

    def "Test list by vehicle unknown"() {
        setup:
        fullTankController.vehicleRepository.findOne(_) >> null

        when:
        def response = mockMvc.perform(MRB.get("${baseUrl}/list/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test save"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle
        fullTankController.fullTankRepository.save(_) >> new FullTank(id: 123, vehicle: vehicle)
        def json = new JsonBuilder(new FullTank(id: 1245)).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("${baseUrl}/save").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("id").value(123))
    }

    def "Test save with vehicle not found"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> null

        when:
        def response = mockMvc.perform(MRB.post("${baseUrl}/save").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test save with no full tank"() {
        setup:
        fullTankController.vehicleRepository.findOne(1) >> vehicle

        when:
        def response = mockMvc.perform(MRB.post("${baseUrl}/save").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string(""))
    }

//    def "Test delete"() {
//        setup:
//        fullTankController.vehicleRepository.findOne(_) >> new Vehicle(id: 1)
//        def json = new JsonBuilder(new Vehicle(registerNumber: 1245)).toPrettyString()
//
//        when:
//        def response = mockMvc.perform(MRB.delete("/vehicle/delete").contentType(MediaType.APPLICATION_JSON).content("1"))
//
//        then:
//        response.andExpect(MRM.status().isOk())
//        response.andExpect(MRM.content().string("Deleted : 1"))
//    }
//
//    def "Test delete with vehicle not found"() {
//        setup:
//        fullTankController.vehicleRepository.findOne(_) >> null
//
//        when:
//        def response = mockMvc.perform(MRB.delete("/vehicle/delete").contentType(MediaType.APPLICATION_JSON).content("1"))
//
//        then:
//        response.andExpect(MRM.status().isNotFound())
//    }
//
//    def "Test find all"() {
//        setup:
//        fullTankController.vehicleRepository.findAll() >> [new Vehicle(id:1, registerNumber: "register1"), new Vehicle(id:2, registerNumber: "register2")]
//
//        when:
//        def response = mockMvc.perform(MRB.get("/vehicle/list"))
//
//        then:
//        response.andExpect(MRM.status().isOk())
//        response.andExpect(MRM.jsonPath("\$").isArray())
//    }
}
