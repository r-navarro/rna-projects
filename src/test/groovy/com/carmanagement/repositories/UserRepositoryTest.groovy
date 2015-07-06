package com.carmanagement.repositories

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
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
        userRepository.save(new User(name: "toto"))
        def user = userRepository.findByName("toto")
        def userNull = userRepository.findByName("tata")

        then:
        user
        !userNull
    }

    def "test user name is unique"() {
        when:
        userRepository.save(new User(name: "toto"))
        userRepository.save(new User(name: "toto"))

        then:
        thrown(DataIntegrityViolationException)
    }
}
