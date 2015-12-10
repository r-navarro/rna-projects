package com.carmanagement.services

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
@Transactional
class UserServiceTest extends Specification {

    public static final String USER_TEST_NAME = "test"

    @Autowired
    UserService userService

    @Autowired
    VehiclesService vehiclesService

    User user

    def setup() {
        userService.findAll().each { userService.delete(it.id) }
        user = userService.save(new User(name: "user"))
    }

    def cleanup() {
        userService.delete(user.id)
    }

    def "test create case"() {

        expect:
        user.id
    }

    def "test create user name already taken case"() {
        setup:
        def userToCreate = new User(name: user.name)

        when:
        userService.save(userToCreate)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: user.name).message
    }

    @Rollback
    def "test update user nominal case"() {
        setup:
        user.accountExpired = true
        user.enabled = false
        user.name = USER_TEST_NAME

        when:
        def result = userService.save(user)

        then:
        !result.enabled
        result.name == USER_TEST_NAME
    }

    @Rollback
    def "test update user change name already taken case"() {
        setup:
        userService.save(new User(name: USER_TEST_NAME))
        user.name = USER_TEST_NAME

        when:
        userService.save(user)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: USER_TEST_NAME).message
    }


    def "test delete user not found"() {
        when:
        userService.delete(-1)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: -1).message
    }

    @Rollback
    def "check user vehicle nominal case"() {
        setup:
        def vehicle = vehiclesService.save(new Vehicle(registerNumber: 1), user)

        when:
        def result = userService.checkUserVehicle(user.name, vehicle)

        then:
        result
    }

    @Rollback
    def "check user vehicle, user name not match"() {
        setup:
        def userTest = userService.save(new User(name: USER_TEST_NAME))
        def vehicle = vehiclesService.save(new Vehicle(registerNumber: 1), userTest)

        when:
        def result = userService.checkUserVehicle(user.name, vehicle)

        then:
        !result
    }


    @Rollback
    def "check user vehicle user not found"() {
        setup:
        def vehicle = vehiclesService.save(new Vehicle(registerNumber: 1), user)

        when:
        def result = userService.checkUserVehicle(USER_TEST_NAME, vehicle)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: USER_TEST_NAME).message
    }

    def "find by name test"() {
        when:
        def result = userService.findByName(user.name)

        then:
        result.name == user.name
    }
}
