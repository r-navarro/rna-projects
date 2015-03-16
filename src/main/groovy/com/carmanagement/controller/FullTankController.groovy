package com.carmanagement.controller

import com.carmanagement.entities.FullTank
import com.carmanagement.repositories.FullTankRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value="/fullTank")
class FullTankController {

    private static final int PAGE_SIZE = 5

    @Autowired
    FullTankRepository fullTankRepository

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    def FullTank create(@RequestBody FullTank fullTank){

        return fullTankRepository.save(fullTank)
    }

    @RequestMapping(value = "list/{vehicleId}/{page}", method = RequestMethod.GET)
    def Page<FullTank> findFullTankByVehiclePaginate(@PathVariable("vehicleId") Integer vehicleId, @PathVariable("page") Integer page){

        PageRequest request = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.DESC, "date")
        return fullTankRepository.findAll(request)
    }
}
