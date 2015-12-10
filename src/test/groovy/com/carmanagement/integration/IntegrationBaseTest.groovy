package com.carmanagement.integration

import com.carmanagement.UiApplication
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Stepwise

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = UiApplication)
@WebIntegrationTest
@ActiveProfiles("test")
@Stepwise
abstract class IntegrationBaseTest extends Specification {

    @Value('${local.server.port}')
    int port

    def templateAdmin = new TestRestTemplate("admin", "admin")

    def templateUser = new TestRestTemplate("toto", "toto")

    def templateNoUser = new TestRestTemplate()

    String getBasePath() { "" }

    URI serviceURI(String path = "") {
        new URI("http://localhost:$port/$basePath$path")
    }

}
