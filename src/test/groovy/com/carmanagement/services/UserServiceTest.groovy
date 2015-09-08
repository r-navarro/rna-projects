package com.carmanagement.services

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.services.impls.UserServiceImpl
import com.carmanagement.services.interfaces.UserService
import spock.lang.Specification

class UserServiceTest extends Specification {

    def userService = new UserServiceImpl()

    def user = new User(id: 1, name: "user1")

    def userDto = new UserDTO(id: 1, name: "user1")

    def userDtoWithIdNull = new UserDTO(name: "newUser1")

    def setup() {
        userService.userRepository = Stub(UserRepository)
    }

    def "test create case"() {
        setup:
        userService.userRepository = Mock(UserRepository)
        userService.userRepository.findByName(_) >> null

        when:
        userService.create(userDtoWithIdNull)

        then:
        1 * userService.userRepository.save(_)
    }

    def "test create user name already taken case"() {
        setup:
        userService.userRepository.findByName(_) >> user

        when:
        userService.create(userDtoWithIdNull)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: userDtoWithIdNull.name).message
    }

    def "test update user nominal case"() {
        setup:
        userService.userRepository = Mock(UserRepository)
        userService.userRepository.findOne(_) >> user
        userService.userRepository.findByName(_) >> null

        when:
        def result = userService.update(userDto)

        then:
        1 * userService.userRepository.save(_)
    }

    def "test update user change name already taken case"() {
        setup:
        userService.userRepository.findOne(_) >> user
        userService.userRepository.findByName(_) >> user

        when:
        userService.update(userDto)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_ALREADY_EXIST, errorParameter: user.name).message
    }

    def "test update user not found case"() {
        setup:
        userService.userRepository.findOne(_) >> null

        when:
        userService.update(userDto)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: user.id).message
    }

    def "test delete user nominal case"() {
        setup:
        userService.userRepository = Mock(UserRepository)
        userService.userRepository.findOne(_) >> user

        when:
        userService.delete(userDto)

        then:
        1 * userService.userRepository.delete(_)
    }

    def "test delete user not found"() {
        setup:
        userService.userRepository.findOne(_) >> null

        when:
        userService.delete(userDto)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: user.id).message
    }

    def "check user vehicle nominal case"() {
        setup:
        def vehicle = new Vehicle(user: user)
        userService.userRepository.findByName(_) >> user

        when:
        def result = userService.checkUserVehicle(user.name, vehicle)

        then:
        result
    }

    def "check user vehicle, user name not match"() {
        setup:
        def vehicle = new Vehicle(user: user)
        userService.userRepository.findByName(_) >> new User(name: "toto")

        when:
        def result = userService.checkUserVehicle(user.name, vehicle)

        then:
        !result
    }

    def "check user vehicle user not found"() {
        setup:
        def vehicle = new Vehicle(user: user)
        userService.userRepository.findByName(_) >> null

        when:
        def result = userService.checkUserVehicle(user.name, vehicle)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: user.name).message
    }
}
