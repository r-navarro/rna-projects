package com.carmanagement.integration

import com.carmanagement.dto.UserDTO
import com.carmanagement.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import spock.lang.Ignore
import spock.lang.Shared

class UserIntegrationTest extends IntegrationBaseTest {

    @Shared
    def userDTO = new UserDTO(name: "test")

    String getBasePath() { "users/" }

    @Shared
    def token

    @Shared
    def ROLE_TO_USER = [
            NO_ROLE: [name: null, password: null],
            USER   : [name: 'toto', password: 'toto'],
            ADMIN  : [name: 'admin', password: 'admin']]

    @Autowired
    UserRepository userRepository


    def "test authentication of endpoint"() {
        given:
        RestTemplate restTemplate = new TestRestTemplate(user.name, user.password)
        RequestEntity request = RequestEntity.get(serviceURI(endpoint)).build()

        when:
        ResponseEntity response = restTemplate.exchange(request, Object)
        token = response.headers.get("Set-Cookie").get(1).split("=")[1].split(";")[0]

        then:
        response.statusCode == status

        where:
        endpoint | user                 | status
        "/login" | ROLE_TO_USER.NO_ROLE | HttpStatus.UNAUTHORIZED
        "/login" | ROLE_TO_USER.USER    | HttpStatus.OK
        "/login" | ROLE_TO_USER.ADMIN   | HttpStatus.OK
        ""       | ROLE_TO_USER.NO_ROLE | HttpStatus.UNAUTHORIZED
        ""       | ROLE_TO_USER.USER    | HttpStatus.OK
        ""       | ROLE_TO_USER.ADMIN   | HttpStatus.OK
        "/all"   | ROLE_TO_USER.NO_ROLE | HttpStatus.UNAUTHORIZED
        "/all"   | ROLE_TO_USER.USER    | HttpStatus.FORBIDDEN
        "/all"   | ROLE_TO_USER.ADMIN   | HttpStatus.OK
    }

    def "test get all user"() {
        when:
        def responseAdmin = templateAdmin.getForEntity("http://localhost:$port/users/all", List)
        def responseUser = templateUser.getForEntity("http://localhost:$port/users/all", Map)
        def responseNoUser = templateNoUser.getForEntity("http://localhost:$port/users/all", Map)

        then:
        responseAdmin.statusCode == HttpStatus.OK
        responseAdmin.body.size() == 2
        responseUser.statusCode == HttpStatus.FORBIDDEN
        responseNoUser.statusCode == HttpStatus.UNAUTHORIZED
    }

    @Ignore
    def "test create user"() {
        setup:
        RequestEntity<UserDTO> request = RequestEntity.post(serviceURI()).body(userDTO)

        when:
        ResponseEntity<UserDTO> responseAdmin = templateAdmin.exchange(request, UserDTO)

        then:
        responseAdmin.statusCode == HttpStatus.CREATED
        responseAdmin.body.name == "test"
    }

    def "test get user"() {
        setup:
        RequestEntity<UserDTO> request = RequestEntity.get(serviceURI()).build()

        when:
        def responseAdmin = templateAdmin.exchange(request, UserDTO)

        then:
        responseAdmin.statusCode == HttpStatus.OK
        responseAdmin.body.name == "admin"
    }

    @Ignore
    def "test update user"() {
        setup:
        def user = userRepository.findByName("toto")
        user.password = "password"
        user.accountLocked = true
        def header = new HttpHeaders()
        header.add("X-XSRF-TOKEN", token)
//        RequestEntity<UserDTO> request = RequestEntity.put(serviceURI()).body(new UserDTO(user))
        RequestEntity<UserDTO> request = new RequestEntity(new UserDTO(user), header, HttpMethod.PUT, serviceURI())

        when:
        ResponseEntity response = templateAdmin.exchange(request, UserDTO)

        then:
        response.statusCode == HttpStatus.CREATED
    }
}
