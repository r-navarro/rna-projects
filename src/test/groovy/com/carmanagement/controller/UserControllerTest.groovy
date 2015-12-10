package com.carmanagement.controller

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.User
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.services.interfaces.UserService
import groovy.json.JsonBuilder
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class UserControllerTest extends AbstractControllerTest {

    UserController userController = new UserController()

    String baseUrl = '/users'

    def user = new User(id:1, name: "user")

    def unknownUser = new User(id:2, name: 'unknown')

    def userDTO = new UserDTO(id:1, name: "user")

    def setup() {
        userController.userService = Stub(UserService)
        userController.userRepository = Stub(UserRepository)
        setupMockMvc(userController)
    }

    def "test get action"(){
        setup:
        userController.userService.findByName(_) >> user

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }


    def "test get by id action"() {
        setup:
        userController.userService.findById(_) >> user

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "test get action admin"(){
        setup:
        setupMockMvcAdminUser(userController)
        userController.userService.findById(_) >> unknownUser

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/2"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(2))
    }

    def "test get action not allowed"(){
        setup:
        userController.userService.findById(_) >> unknownUser

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isUnauthorized())
    }

    def "test get action user not found"(){
        setup:
        userController.userService.findById(_) >> null

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: user.id).getMessage()))
    }

    def "update test"() {
        setup:
        userController.userService.findById(_) >> user
        userController.userService.save(_) >> user
        def json = new JsonBuilder(userDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$user.id").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isCreated())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "update test user not found"() {
        setup:
        userController.userService.findById(_) >> null
        def json = new JsonBuilder(userDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$user.id").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: user.id).getMessage()))
    }

    def "update test not allowed"() {
        setup:
        userController.userService.findById(_) >> unknownUser
        def json = new JsonBuilder(userDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.put("$baseUrl/$user.id").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isUnauthorized())
    }

    def "Test delete"() {
        setup:
        userController.userService.findById(_) >> user

        when:
        def response = mockMvc.perform(MRB.delete("$baseUrl/$user.id"))

        then:
        response.andExpect(MRM.status().isNoContent())
    }


    def "Test create"() {
        setup:
        userController.userService.save(_) >> user
        def json = new JsonBuilder(userDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isCreated())
        response.andExpect(MRM.jsonPath("id").value(1))
    }

    def "Test create user not available"() {
        setup:
        userController.userService.save(_) >> {
            throw new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: userDTO.name)
        }
        def json = new JsonBuilder(userDTO).toPrettyString()

        when:
        def response = mockMvc.perform(MRB.post("$baseUrl").contentType(MediaType.APPLICATION_JSON).content(json))

        then:
        response.andExpect(MRM.status().isNotFound())
        response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: userDTO.name).message))
    }
}
