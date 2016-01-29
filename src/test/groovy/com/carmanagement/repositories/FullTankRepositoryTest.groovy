package com.carmanagement.repositories

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import groovy.time.TimeCategory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class FullTankRepositoryTest extends Specification {

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    @Autowired
    UserRepository userRepository


    def cleanup() {
        fullTankRepository.deleteAll()
        vehicleRepository.deleteAll()
        userRepository.deleteAll()
    }


    def "full tank repository is not null"() {
        expect:
        fullTankRepository
    }

    def "find full tank by vehicle test"() {
        when:
        def user = userRepository.save(new User(name: "test"))
        def vehicle = vehicleRepository.save(new Vehicle(registerNumber: 1, user: user))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 1))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 2))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 3))

        PageRequest request = new PageRequest(0, 3, Sort.Direction.DESC, "date")

        Page fullTanks = fullTankRepository.findByVehicleId(vehicle.id, request)

        then:
        fullTanks.getSize() == 3

    }

    def "find full tank by vehicle limit case test"() {
        when:
        def fullTanks = fullTankRepository.findByVehicleId(null, null)

        then:
        fullTanks.getSize() == 0
    }

    def "find all by vehicle test"() {
        setup:
        def user = userRepository.save(new User(name: "test"))
        def vehicle = vehicleRepository.save(new Vehicle(registerNumber: 1, user: user))
        5.times {
            fullTankRepository.save(new FullTank(vehicle: vehicle, cost: it))
        }

        when:
        def fullTanks = fullTankRepository.findAllByVehicleId(vehicle.id)

        then:
        fullTanks.size() == 5
    }

    def "find all order by vehicle test"() {
        setup:
        def user = userRepository.save(new User(name: "test"))
        def vehicle = vehicleRepository.save(new Vehicle(registerNumber: 1, user: user))
        def date = new Date()
        def now = new Date()
        5.times {
            fullTankRepository.save(new FullTank(vehicle: vehicle, cost: it, date: date))
            use(TimeCategory){date = date - 10.days}
        }

        when:
        def fullTanks = fullTankRepository.findAllByVehicleIdOrderByDateAsc(vehicle.id)

        then:
        fullTanks.size() == 5
        fullTanks.last().date.time == now.time
    }
}
