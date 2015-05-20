package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import groovy.json.JsonBuilder
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
import spock.lang.Specification

import java.lang.reflect.Method

@ContextConfiguration(classes = PersistenceTestConfig.class)
@EnableWebMvc
@ActiveProfiles("test")
class VehicleControllerTest extends Specification {

    MockMvc mockMvc

    VehicleController vehicleController

    def setup() {
        vehicleController = new VehicleController()
        vehicleController.vehicleRepository = Mock(VehicleRepository)
        vehicleController.userRepository = Mock(UserRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).setHandlerExceptionResolvers(createExceptionResolver()).build()
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

    def "Test save"() {
        setup:
        vehicleController.vehicleRepository.save(_) >> new Vehicle(id: 1)
        def json = new JsonBuilder(new Vehicle(registerNumber: 1245)).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("/vehicle/save").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test save with no vehicle"() {
        when:
        def response = mockMvc.perform(MRB.post("/vehicle/save").contentType(MediaType.APPLICATION_JSON).content("{}"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string(""))
    }

    def "Test delete"() {
        setup:
        vehicleController.vehicleRepository.findOne(_) >> new Vehicle(id: 1)

        when:
        def response = mockMvc.perform(MRB.delete("/vehicle/delete").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string("Deleted : 1"))
    }

    def "Test delete with vehicle not found"() {
        setup:
        vehicleController.vehicleRepository.findOne(_) >> null

        when:
        def response = mockMvc.perform(MRB.delete("/vehicle/delete").contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(MRM.status().isNotFound())
    }

    def "Test find all"() {
        setup:
        vehicleController.vehicleRepository.findAll() >> [new Vehicle(id: 1, registerNumber: "register1"), new Vehicle(id: 2, registerNumber: "register2")]

        when:
        def response = mockMvc.perform(MRB.get("/vehicle/list"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.jsonPath("\$").isArray())
    }
}
