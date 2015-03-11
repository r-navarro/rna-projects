package com.carmanagement.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

import com.carmanagement.config.PersistenceTestConfig
import com.carmanagement.entities.Vehicle


@ContextConfiguration(classes=PersistenceTestConfig.class)
@ActiveProfiles("test")
class VehicleRepositoryTest extends Specification {

	@Autowired
	VehicleRepository vehicleRepository

	def "test repository is not null"(){
		expect:
		vehicleRepository
	}

	def "test repository find by register number"(){
		when:
		vehicleRepository.save(new Vehicle(registerNumber:"r1"))

		def vehicle = vehicleRepository.findByRegisterNumber("r1")
		def vehicleNull = vehicleRepository.findByRegisterNumber("r2")

		then:
		vehicle
		vehicle.registerNumber == "r1"
		!vehicleNull
	}
}
