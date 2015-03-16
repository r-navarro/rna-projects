package com.carmanagement.repositories

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.FullTank
import com.carmanagement.entities.Vehicle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@ContextConfiguration(classes=PersistenceTestConfig.class)
@ActiveProfiles("test")
class FullTankRepositoryTest extends Specification{

    @Autowired
    FullTankRepository fullTankRepository

    @Autowired
    VehicleRepository vehicleRepository

    def "full tank repository is not null"(){
        expect:
        fullTankRepository
    }

    def "find full tank by vehicle test"(){
        when :
        def vehicle = vehicleRepository.save(new Vehicle(registerNumber: 1))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 1))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 2))
        fullTankRepository.save(new FullTank(vehicle: vehicle, cost: 3))

        def fullTanks = fullTankRepository.findByVehicle(vehicle)

        then:
        fullTanks.size() == 3
        fullTanks.collect{it.cost}.sum() == 6

    }

    def "find full tank by vehicle limit case test"(){
        when :
        def fullTanks = fullTankRepository.findByVehicle(null)

        then:
        fullTanks.size() == 0
    }
}
