package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors as RPP
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as MRB
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@ContextConfiguration(classes = PersistenceTestConfig)
@WebAppConfiguration
@ActiveProfiles("test")
class Test extends Specification {

    @Autowired
    WebApplicationContext context

    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build()
    }

    def "test 1"() {
        when:
            def response = mvc.perform(MRB.get('/isAlive'))
        then:
            response.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "test 2"() {
        when:
            def response = mvc.perform(MRB.get('/home').with(RPP.user("admin").password("pass").roles("USER", "ADMIN")))
        then:
            response.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "test 3"() {
        when:
            def response = mvc.perform(MRB.get('/vehicles/list').with(RPP.httpBasic("admin", 'admin')))
        then:
            response.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "test 4"() {
        when:
            def response = mvc.perform(MRB.get('/vehicles/list').with(RPP.httpBasic('toto', 'toto')))
        then:
            response.andExpect(MockMvcResultMatchers.status().isForbidden())
    }


    def "test 5"() {
        when:
            def response = mvc.perform(MRB.post('/vehicles')
                    .with(RPP.httpBasic('toto', 'toto'))
                    .with(RPP.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content('{"registrationNumber":"test", "price":"1", "type":"test", "kilometers":"1"}'))
        then:
            response.andExpect(MockMvcResultMatchers.status().isCreated())
    }
}
