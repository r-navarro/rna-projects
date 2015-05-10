package com.carmanagement.controller

import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.UserNotFoundException
import com.carmanagement.exceptions.VehicleNotFoundException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.repositories.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value="/vehicle")
@Secured("ROLE_USER")
class VehicleController {

	private static final int PAGE_SIZE = 2

	@Autowired
	private VehicleRepository vehicleRepository

    @Autowired
    private UserRepository userRepository

    @RequestMapping(value = "/save", method=RequestMethod.POST)
	def String create(@RequestBody Vehicle vehicle){

		Vehicle savedVehicle = vehicleRepository.save(vehicle)

		return "hello : ${savedVehicle?.id}"
	}

	@RequestMapping(value = "/delete", method=RequestMethod.DELETE)
	def String delete(@RequestBody Long id){

		def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            vehicleRepository.delete(vehicle)

            return "Deleted : ${id}"
        }
        throw new VehicleNotFoundException(i)
	}

	@RequestMapping(value = "/list", method=RequestMethod.GET)
    @Secured("ROLE_ADMIN")
	def Iterable<Vehicle> findAll(){

		return vehicleRepository.findAll()
	}

	@RequestMapping(value = "/get/{id}", method=RequestMethod.GET)
	def Vehicle get(@PathVariable Long id){
        def vehicle = vehicleRepository.findOne(id)

        if (vehicle) {
            return vehicle
        }
        throw new VehicleNotFoundException(id)
	}

    @RequestMapping(value = "/list/{userId}/{page}", method = RequestMethod.GET)
    def Page<Vehicle> getVehicle(@PathVariable Long userId, @PathVariable Integer page) {
        if (!userRepository.findOne(userId)) {
            throw new UserNotFoundException(userId)
        }
		PageRequest request = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.DESC, "registerNumber")
        def result = vehicleRepository.findAllByUserId(userId, request)
        return result
	}
}
