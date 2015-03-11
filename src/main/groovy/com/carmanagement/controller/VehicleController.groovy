package com.carmanagement.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.carmanagement.entities.Vehicle
import com.carmanagement.repositories.VehicleRepository

@RestController
@RequestMapping(value="/vehicle")
class VehicleController {

	private static final int PAGE_SIZE = 2

	@Autowired
	private VehicleRepository vehicleRepository

	@RequestMapping(value = "/save", method=RequestMethod.POST)
	def String create(@RequestBody Vehicle vehicle){

		Vehicle savedVehicle = vehicleRepository.save(vehicle)

		return "hello : ${savedVehicle?.id}"
	}

	@RequestMapping(value = "/delete", method=RequestMethod.DELETE)
	def String delete(@RequestBody Long id){

		def vehicle = vehicleRepository.findOne(id)

		Vehicle savedVehicle = vehicleRepository.delete(vehicle)

		return "Deleted : ${id}"
	}

	@RequestMapping(value = "/list", method=RequestMethod.GET)
	def List<Vehicle> findAll(){

		return vehicleRepository.findAll()
	}

	@RequestMapping(value = "/get/{id}", method=RequestMethod.GET)
	def Vehicle get(@PathVariable Long id){

		return vehicleRepository.findOne(id)
	}

	@RequestMapping(value = "/list/{page}", method=RequestMethod.GET)
	def Page<Vehicle> getVehicle(@PathVariable Integer page) {
		PageRequest request = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.DESC, "registerNumber")
		return vehicleRepository.findAll(request)
	}
}
