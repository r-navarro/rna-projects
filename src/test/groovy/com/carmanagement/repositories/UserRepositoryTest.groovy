package com.carmanagement.repositories

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class UserRepositoryTest extends Specification {

    @Autowired
    UserRepository userRepository

    def cleanup() {
        userRepository.deleteAll()
    }


    def "test userRepository is not null"() {
        expect:
        userRepository
    }

    def "test find by username"() {
        when:
        userRepository.save(new User(username: "toto"))
        def user = userRepository.findByUsername("toto")
        def userNull = userRepository.findByUsername("tata")

        then:
        user
        !userNull
    }
}
