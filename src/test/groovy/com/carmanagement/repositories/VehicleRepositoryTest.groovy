package com.carmanagement.repositories

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class VehicleRepositoryTest extends Specification {

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserRepository userRepository

    def cleanup() {
        vehicleRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "test repository is not null"() {
        expect:
        vehicleRepository
    }

    def "test repository find by register number"() {
        when:
        vehicleRepository.save(new Vehicle(registerNumber: "r1", user: userRepository.save(new User(name: "toto"))))

        def vehicle = vehicleRepository.findByRegisterNumber("r1")
        def vehicleNull = vehicleRepository.findByRegisterNumber("r2")

        then:
        vehicle
        vehicle.registerNumber == "r1"
        !vehicleNull
    }

    def "test find all by user"() {
        setup:
        def user = userRepository.save(new User(name: "test"))
        def user2 = userRepository.save(new User(name: "toto"))
        (0..10).each {
            if (it % 2 == 0) {
                vehicleRepository.save(new Vehicle(user: user, registerNumber: it))
            } else {
                vehicleRepository.save(new Vehicle(registerNumber: it, user: user2))
            }
        }
        PageRequest pageable = new PageRequest(0, 10, Sort.Direction.DESC, "registerNumber")

        when:
        def result = vehicleRepository.findAllByUserId(user.id, pageable)

        then:
        result.totalElements == 6
        result.content.collect { it.registerNumber }.sort().join(",") == "0,10,2,4,6,8"

    }

    def "test find all by user unknown"() {
        setup:
        PageRequest pageable = new PageRequest(0, 10, Sort.Direction.DESC, "registerNumber")

        when:
        def result = vehicleRepository.findAllByUserId(1, pageable)
        def resultByName = vehicleRepository.findAllByUserName("name", pageable)

        then:
        !result.totalElements
        !resultByName.totalElements

    }

    def "test find all by user name"() {
        setup:
        def user = userRepository.save(new User(name: "test"))
        def user2 = userRepository.save(new User(name: "toto"))
        (0..10).each {
            if (it % 2 == 0) {
                vehicleRepository.save(new Vehicle(user: user, registerNumber: it))
            } else {
                vehicleRepository.save(new Vehicle(registerNumber: it, user: user2))
            }
        }
        PageRequest pageable = new PageRequest(0, 10, Sort.Direction.DESC, "registerNumber")

        when:
        def result = vehicleRepository.findAllByUserName(user.name, pageable)

        then:
        result.totalElements == 6
        result.content.collect { it.registerNumber }.sort().join(",") == "0,10,2,4,6,8"

    }
}
