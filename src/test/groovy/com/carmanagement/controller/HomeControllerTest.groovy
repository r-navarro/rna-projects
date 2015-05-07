package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as MRM
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class HomeControllerTest extends Specification {

    MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HomeController()).build()
    }

    def "Test simple controller get action"() {
        when:
        def response = mockMvc.perform(MRB.get("/home"))

        then :
        response.andExpect(MRM.status().isOk())
        response.andExpect(MRM.content().string("index"))
    }
}
