package com.carmanagement.integration

import com.carmanagement.UiApplication
import com.carmanagement.controller.pojo.ErrorResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


@ContextConfiguration(loader = SpringApplicationContextLoader, classes = UiApplication)
@WebIntegrationTest
@ActiveProfiles("test")
class HomeIntegrationTest extends Specification {

    @Value('${local.server.port}')
    int port

    RestTemplate template = new TestRestTemplate();


    def "test home controller"() {
        setup:
        def a = template.getForEntity("http://localhost:$port/home", ErrorResponse)

        expect:
        a.statusCode == HttpStatus.UNAUTHORIZED
    }
}
