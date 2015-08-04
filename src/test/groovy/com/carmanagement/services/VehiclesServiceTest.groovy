package com.carmanagement.services

import com.carmanagement.dto.VehicleDTO
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import com.carmanagement.services.impls.VehiclesServiceImpl
import com.carmanagement.services.interfaces.UserService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class VehiclesServiceTest extends Specification {

    VehiclesServiceImpl vehiclesService

    Vehicle vehicle = new Vehicle(id: 1, kilometers: 1, type: "test", price: 1, registerNumber: "test")

    VehicleDTO vehicleDTO = new VehicleDTO(id: 1, kilometers: 1, type: "testDTO", price: 1, registerNumber: "testDTO")

    def setup() {
        vehiclesService = new VehiclesServiceImpl()
        vehiclesService.vehicleRepository = Stub(VehicleRepository)
        vehiclesService.userService = Stub(UserService)
        vehiclesService.userRepository = Stub(UserRepository)
    }

    def "Test get"() {
        setup:
        vehiclesService.vehicleRepository.findOne(_) >> vehicle
        vehiclesService.userService.checkUserVehicle(_, _) >> true

        when:
        def result = vehiclesService.get(1, "user")

        then:
        result
    }

    def "Test get with user not matching"() {
        setup:
        vehiclesService.vehicleRepository.findOne(_) >> vehicle
        vehiclesService.userService.checkUserVehicle(_, _) >> false

        when:
        def result = vehiclesService.get(1, "user")

        then:
        !result
    }

    def "Test get with vehicle not found"() {
        setup:
        vehiclesService.vehicleRepository.findOne(_) >> null

        when:
        def result = vehiclesService.get(1, "user")

        then:
        !result
    }

    def "Test save"() {
        setup:
        vehiclesService.vehicleRepository.save(_) >> vehicle

        when:
        def result = vehiclesService.save(vehicleDTO, new User())

        then:
        result.registerNumber == "test"
    }

    def "test get vehicles"(){
        setup:
        def vehicles = []
        3.times{
            vehicles << new Vehicle(id:it)
        }
        vehiclesService.vehicleRepository.findAllByUserName(_, _) >> new PageImpl<Vehicle>(vehicles)

        when:
        def result = vehiclesService.getVehicles(new PageRequest(1, 1), "test")

        then:
        result.content.size() == vehicles.size()
        result.content.collect{it.id}.join("") == "012"
    }

    def "test get 0 vehicles"(){
        setup:
        vehiclesService.vehicleRepository.findAllByUserName(_, _) >> new PageImpl<Vehicle>([])

        when:
        def result = vehiclesService.getVehicles(new PageRequest(1, 1), "test")

        then:
        result.content.size() == 0
    }

    def "test delete"() {
        given:
        vehiclesService.vehicleRepository = Mock(VehicleRepository)
        vehiclesService.vehicleRepository.findOne(_) >> vehicle

        when:
        vehiclesService.delete(1)

        then:
        1 * vehiclesService.vehicleRepository.delete(vehicle)
    }

    def "test delete with vehicle not found"() {
        given:
        vehiclesService.vehicleRepository.findOne(_) >> null

        when:
        vehiclesService.delete(1)

        then:
        def ex = thrown TechnicalException
        ex.getMessage() == new TechnicalException(errorCode: ErrorCode.VEHICLE_NOT_FOUND, errorParameter: 1).message
    }

    def "test find all"() {
        given:
        def vehicles = []
        3.times{
            vehicles << new Vehicle(id:it)
        }
        vehiclesService.vehicleRepository.findAll() >> vehicles

        when:
        def result = vehiclesService.findAll()

        then:
        result.size() == vehicles.size()
        result.collect{it.id}.join("") == "012"
    }
}
