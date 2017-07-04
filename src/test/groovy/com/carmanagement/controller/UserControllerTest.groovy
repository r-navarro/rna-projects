package com.carmanagement.controller

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.User
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import spock.lang.Shared

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    UserRepository userRepository

    String baseUrl = '/users'

    @Shared
    def user = new User(name: "user")

    @Shared
    def userToto

    def unknownUser = new User(id: 2, name: 'unknown')

    def userDTO = new UserDTO(id: 1, name: "user")

    def init() {
        user = userRepository.save(user)
        userToto = userRepository.findByName('toto')
    }


    def "test get action"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl, adminHttpBasic()))
            def resultJson = getResponseAsJsonObject(response) as List

        then:
            response.andExpect(MRM.status().isOk())
            response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
            resultJson.size() == 3
    }


    def "test get by id not admin"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl + "/$user.id", totoHttpBasic()))
            def resultJson = getResponseAsJsonObject(response)
        then:
            response.andExpect(MRM.status().isUnauthorized())
            response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
            resultJson.id != user.id
    }

    def "test get by is as admin"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl + "/$user.id", adminHttpBasic()))
            def resultJson = getResponseAsJsonObject(response)
        then:
            response.andExpect(MRM.status().isOk())
            response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
            resultJson.id == user.id
    }

    def "test get by id as user"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl + "/$userToto.id", totoHttpBasic()))
            def resultJson = getResponseAsJsonObject(response)
        then:
            response.andExpect(MRM.status().isOk())
            response.andExpect(MRM.content().contentType("${MediaType.APPLICATION_JSON};charset=UTF-8"))
            resultJson.id == userToto.id
    }

    def "test get action user not found"() {
        when:
            def response = mockMvc.perform(getGetQuery(baseUrl + "/0", adminHttpBasic()))

        then:
            response.andExpect(MRM.status().isBadRequest())
            response.andExpect(MRM.jsonPath("errorMessage").value(new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: 0).getMessage()))
    }
}
