package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.controller.utils.QueryUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@ContextConfiguration(classes = PersistenceTestConfig)
@WebAppConfiguration
@ActiveProfiles("test")
abstract class AbstractControllerTest extends Specification implements QueryUtils {

    @Autowired
    WebApplicationContext context

    @Shared
    MockMvc mockMvc

    @Shared
    def firstTest = true;

    def setup() {
        if (firstTest) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build()
            init()
            firstTest = false
        }
    }

    def setupSpec() {
        firstTest = true
    }

    def init() {

    }

    def getUserAdmin() {
        return SecurityMockMvcRequestPostProcessors.user("admin").password("pass").roles("USER", "ADMIN")
    }

    def adminHttpBasic() {
        return httpBasic("admin", 'admin')
    }

    def totoHttpBasic() {
        return httpBasic("toto", 'toto')
    }

    def httpBasic(String user, String pass) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user, pass)
    }
}
