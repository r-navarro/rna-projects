package com.carmanagement.services

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.Maintenance
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.services.interfaces.MaintenancesService
import com.carmanagement.services.interfaces.UserService
import com.carmanagement.services.interfaces.VehiclesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
@Transactional
class MaintenancesServiceTest extends Specification {

    @Autowired
    MaintenancesService maintenancesService

    @Autowired
    VehiclesService vehiclesService

    @Autowired
    UserService userService

    def user = new User(name: "user")

    @Shared
    def vehicle = new Vehicle(registerNumber: "test", kilometers: 1000, actions: [])

    @Shared
    def maintenance = new Maintenance(distance: 100, cost: 100, date: new Date())


    def setup() {
        vehicle = new Vehicle(registerNumber: "test", kilometers: 1000, actions: [])
        maintenance = new Maintenance(distance: 100, cost: 100, date: new Date())
        user = userService.save(user)
        vehicle = vehiclesService.save(vehicle, user)
        maintenance = maintenancesService.save(maintenance, vehicle.id)
    }

    def cleanup() {
        userService.delete(user.id)
    }


    def "test get"() {
        when:
        def result = maintenancesService.getByVehicleIdAndId(vehicle.id, maintenance.id)

        then:
        result
        result.id == maintenance.id
        result.vehicle.id == vehicle.id
        result.vehicle.user.id == user.id

    }

    def "test get with null params"() {
        when:
        def result = maintenancesService.getByVehicleIdAndId(null, null)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: null).message
    }

    def "test get with wrong vehicle"() {
        when:
        def result = maintenancesService.getByVehicleIdAndId(vehicle.id + 1, maintenance.id)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: (vehicle.id + 1)).message

    }

    @Rollback
    def "get maintenances"() {
        setup:
        5.times {
            maintenancesService.save(new Maintenance(distance: 100 + it, cost: 100, date: new Date()), vehicle.id)
        }

        when:
        def result = maintenancesService.getMaintenances(new PageRequest(0, 10), vehicle.id)

        then:
        result.content.size() == 6
    }

    def "get maintenances with vehicle not found"() {
        when:
        def result = maintenancesService.getMaintenances(new PageRequest(0, 10), vehicle.id + 1)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicle.id + 1).message
    }

    @Rollback
    def "test update"() {
        when:
        maintenance.cost = System.currentTimeMillis()
        def result = maintenancesService.save(maintenance, maintenance.vehicle.id)

        then:
        result.cost == maintenance.cost

    }

    def "test save's errors"() {
        when:
        maintenancesService.save(maintenanceVar, vehicleId)

        then:
        def ex = thrown TechnicalException
        ex.message == message

        where:
        maintenanceVar | vehicleId      | message
        null           | null           | new TechnicalException(errorCode: ErrorCode.MAINTENANCE_WRONG_FORMAT).message
        maintenance    | vehicle.id + 1 | new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: vehicle.id + 1).message
    }

    def "test delete"() {
        when:
        maintenancesService.delete(vehicle.id, maintenance.id)
        def result = maintenancesService.getByVehicleIdAndId(vehicle.id, maintenance.id)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: maintenance.id).message
    }

    def "test delete errors case"() {
        when:
        maintenancesService.delete(vehicleId, maintenanceId)

        then:
        def ex = thrown TechnicalException
        ex.message == message

        where:
        vehicleId  | maintenanceId      | message
        null       | null               | new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: null).message
        vehicle.id | maintenance.id + 1 | new TechnicalException(errorCode: ErrorCode.MAINTENANCE_NOT_FOUND, errorParameter: maintenance.id + 1).message
    }

    def "test delete errors case vehicle not match"() {
        when:
        maintenancesService.delete(vehicle.id + 1, maintenance.id)

        then:
        def ex = thrown TechnicalException
        ex.message == new TechnicalException(errorCode: ErrorCode.MAINTENANCE_VEHICLE_NOT_MATCH, errorParameter: vehicle.id + 1).message
    }

}
