package com.carmanagement.services

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.FullTanksService
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import groovy.time.TimeCategory
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
class FullTanksServiceTest extends Specification {

    @Autowired
    FullTanksService fullTanksService

    @Autowired
    VehiclesService vehiclesService

    @Autowired
    UserService userService

    def user = new User(name: "user")

    Vehicle vehicle = new Vehicle(registerNumber: "test", kilometers: 1000, actions: [])

    FullTank fullTank = new FullTank(distance: 10, quantity: 10, cost: 10, date: new Date())

    def setup() {
        user = userService.save(user)
        vehicle = vehiclesService.save(vehicle, user)
        fullTank = fullTanksService.save(fullTank, vehicle.id)
    }

    def cleanup() {
        userService.delete(user.id)
    }

    @Transactional
    def "test get fullTank"() {
        when:
        def result = fullTanksService.getByVehicleIdAndId(vehicle.id, fullTank.id)

        then:
        result.id
        result.vehicle.id == vehicle.id
        result.vehicle.user.id == user.id
    }

    def "test get fullTank with wrong fullTank id"() {
        when:
        def result = fullTanksService.getByVehicleIdAndId(vehicle.id, fullTank.id + 1)

        then:
        !result
    }

    def "test get fullTank with wrong vehicle id"() {

        when:
        fullTanksService.getByVehicleIdAndId(vehicle.id + 1, fullTank.id)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: (vehicle.id + 1)).message
    }

    @Rollback
    @Transactional
    def "get fullTanks paginate"() {
        setup:
        5.times {
            fullTanksService.save(new FullTank(distance: it + 1, quantity: 1, cost: 1, date: new Date()), vehicle.id)
        }

        when:
        def result = fullTanksService.getFullTanks(new PageRequest(0, 10), vehicle.id)

        then:
        result.content.size() == 6

    }

    def "get fullTanks paginate with vehicle not found"() {
        when:
        fullTanksService.getFullTanks(new PageRequest(0, 10), vehicle.id + 1)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicle.id + 1).message
    }

    @Rollback
    @Transactional
    def "test save update nominal case"() {
        when:
        fullTank.distance = 1000
        def result = fullTanksService.save(fullTank, vehicle.id)

        then:
        result
        result.vehicle.kilometers == 1010

    }

    def "test save vehicle not found test"() {
        when:
        fullTanksService.save(fullTank, vehicle.id + 1)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicle.id + 1).message
    }

    def "test save with bad fullTank"() {
        when:
        fullTanksService.save(new FullTank(), vehicle.id)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.FULL_TANK_WRONG_FORMAT).message
    }

    @Rollback
    @Transactional
    def "test delete"() {
        when:
        fullTanksService.delete(vehicle.id, fullTank.id)

        then:
        !fullTanksService.getByVehicleIdAndId(fullTank.id, vehicle.id)
    }

    def "test delete fullTank not found"() {

        when:
        fullTanksService.delete(vehicle.id, fullTank.id + 1)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.FULL_TANK_NOT_FOUND, errorParameter: fullTank.id + 1).message
    }

    def "test delete fullTank not matching with the vehicle"() {

        when:
        fullTanksService.delete(vehicle.id + 1, fullTank.id)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH, errorParameter: vehicle.id + 1).message
    }

    @Rollback
    @Transactional
    def "Test cost stats"() {
        setup:
        def date1 = new Date()
        def date2 = new Date()
        use(TimeCategory) {
            date2 = date1 + 1.month
        }
        def fullTankModify = new FullTank(cost: 2, date: date2, distance: 0)
        fullTankModify.metaClass.asBoolean = { -> return true }
        fullTanksService.save(fullTankModify, vehicle.id)

        fullTankModify = new FullTank(cost: 1, date: date1, distance: 0)
        fullTankModify.metaClass.asBoolean = { -> return true }
        fullTanksService.save(fullTankModify, vehicle.id)

        when:
        def stats = fullTanksService.getCostStats(vehicle.id)

        then:
        assert stats
        assert stats[0].value == "$fullTank.cost".toString()
        assert stats[1].value == "${1f}".toString()
        assert stats[2].value == "${2f}".toString()
    }

    @Rollback
    @Transactional
    def "Test distance stats"() {
        setup:
        def date1 = new Date()
        def date2 = new Date()
        use(TimeCategory) {
            date2 = date1 + 1.month
        }

        def fullTankModify = new FullTank(distance: 125.62, date: date2)
        fullTankModify.metaClass.asBoolean = { -> return true }
        fullTanksService.save(fullTankModify, vehicle.id)

        fullTankModify = new FullTank(distance: 564.23, date: date1)
        fullTankModify.metaClass.asBoolean = { -> return true }
        fullTanksService.save(fullTankModify, vehicle.id)


        when:
        def stats = fullTanksService.getDistanceStats(vehicle.id)

        then:
        assert stats
        assert stats[0].value == "${fullTank.distance}".toString()
        assert stats[1].value == "${564.23f}".toString()
        assert stats[2].value == "${125.62f}".toString()
    }

    @Rollback
    @Transactional
    def "Test average stats"() {
        setup:
        use(TimeCategory) {
            def now = new Date()
            10.times {
                now += 10.days
                def fullTankModify = new FullTank(distance: 10, quantity: 10, cost: 10, date: now)
                fullTanksService.save(fullTankModify, vehicle.id)
            }
        }

        when:
        def stats = fullTanksService.getAverageStats(vehicle.id)

        then:
        assert stats
        assert stats[0].value == 10
        assert stats[1].value == 10
        assert stats[2].value > 9.8
        assert stats[3].value == 10
    }
}
