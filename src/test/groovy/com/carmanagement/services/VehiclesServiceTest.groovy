package com.carmanagement.services

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
@Transactional
class VehiclesServiceTest extends Specification {

    @Autowired
    VehiclesService vehiclesService

    @Autowired
    UserService userService

    Vehicle vehicle = new Vehicle(kilometers: 1, type: "test", price: 1, registerNumber: "test")

    def user = new User(name: "user")

    def setup() {
        assert vehiclesService.findAll().size() == 0
        user = userService.save(user)
        vehicle = vehiclesService.save(vehicle, user)
    }

    def cleanup() {
        userService.delete(user.id)
    }

    def "Test get"() {
        when:
        def result = vehiclesService.get(vehicle.id, "user")

        then:
        result
    }

    @Rollback
    def "Test get with user not matching"() {
        when:
        def userTest = userService.save(new User(name: "test"))
        def result = vehiclesService.get(vehicle.id, userTest.name)

        then:
        !result
    }

    def "Test get with vehicle not found"() {
        when:
        def result = vehiclesService.get(1, "Nobody")

        then:
        !result
    }

    @Rollback
    def "Test save"() {
        setup:
        def vehicleToCreate = new Vehicle(registerNumber: "test save")

        when:
        def result = vehiclesService.save(vehicleToCreate, user)
        def userVehicleCount = userService.findById(user.id).vehicles.size()

        then:
        result.id
        userVehicleCount == 2
    }

    @Rollback
    def "test get vehicles"(){
        setup:
        3.times{
            vehiclesService.save(new Vehicle(registerNumber: it), user)
        }

        when:
        def result = vehiclesService.getVehicles(new PageRequest(0, 10), user.name)

        then:
        result.content.size() == 4
        result.content.collect { it.registerNumber }.join("") contains "012"
    }

    def "test get 0 vehicles"(){
        when:
        def result = vehiclesService.getVehicles(new PageRequest(1, 1), "test")

        then:
        result.content.size() == 0
    }

    @Rollback
    def "test delete"() {
        when:
        vehiclesService.delete(vehicle.id)

        then:
        !vehiclesService.findAll()
        !userService.findById(user.id).vehicles
    }

    def "test delete with vehicle not found"() {
        when:
        vehiclesService.delete(-1)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: -1).message
    }

    @Rollback
    def "test find all"() {
        setup:
        3.times{
            vehiclesService.save(new Vehicle(registerNumber: it), user)
        }


        when:
        def result = vehiclesService.findAll()

        then:
        result.size() == 4
        result.collect { it.registerNumber }.join("") contains "012"
    }
}
