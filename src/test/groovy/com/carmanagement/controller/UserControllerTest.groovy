package com.carmanagement.controller

import com.carmanagement.entities.User
import com.carmanagement.repositories.UserRepository
import com.carmanagement.services.interfaces.UserService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM

class UserControllerTest extends AbstractControllerTest {

    UserController userController

    String baseUrl = '/users'

    def user = new User(id:1, name: "user")

    def setup() {
        userController = new UserController()
        userController.userService = Stub(UserService)
        userController.userRepository = Stub(UserRepository)
        setupMockMvc(userController)
    }

    def "test get action"(){
        setup:
        userController.userRepository.findOne(_) >> user

        when:
        def response = mockMvc.perform(MRB.get("$baseUrl/1"))

        then:
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
        response.andExpect(MRM.jsonPath("id").value(1))
    }
}
