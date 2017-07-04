package com.carmanagement.controller

import com.carmanagement.dto.MaintenanceDTO
import com.carmanagement.services.interfaces.MaintenancesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = "/vehicles/{vehicleId}")
@Secured("ROLE_USER")
@Slf4j
class MaintenanceController {

    static final int PAGE_SIZE = 5

    @Autowired
    MaintenancesService maintenancesService

    @RequestMapping(value = "/maintenances/{maintenanceId}", method = RequestMethod.GET)
    ResponseEntity<MaintenanceDTO> get(
            @PathVariable("vehicleId") String vehicleId, @PathVariable("maintenanceId") String maintenanceId) {
        def maintenance = maintenancesService.getByVehicleIdAndId(vehicleId, maintenanceId)
        return new ResponseEntity(new MaintenanceDTO(maintenance), HttpStatus.OK)
    }

    @RequestMapping(value = "/maintenances", method = RequestMethod.POST)
    ResponseEntity<MaintenanceDTO> save(
            @PathVariable("vehicleId") String vehicleId, @RequestBody MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO) {
            def maintenance = maintenancesService.save(maintenanceDTO.toMaintenance(), vehicleId)
            return new ResponseEntity(new MaintenanceDTO(maintenance), HttpStatus.CREATED)
        }
        return null
    }

    @RequestMapping(value = "/maintenances/{maintenanceId}", method = RequestMethod.PUT)
    ResponseEntity<MaintenanceDTO> update(
            @PathVariable("vehicleId") String vehicleId,
            @PathVariable("maintenanceId") String maintenanceId, @RequestBody MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO) {
            maintenanceDTO.id = maintenanceId
            def maintenance = maintenancesService.save(maintenanceDTO.toMaintenance(), vehicleId)
            return new ResponseEntity(new MaintenanceDTO(maintenance), HttpStatus.CREATED)
        }
        return null
    }

    @RequestMapping(value = "/maintenances/{maintenanceId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("vehicleId") String vehicleId, @PathVariable("maintenanceId") String maintenanceId) {
        maintenancesService.delete(vehicleId, maintenanceId)
    }

    @RequestMapping(value = "/maintenances", method = RequestMethod.GET)
    Page<MaintenanceDTO> getMaintenances(
            @PathVariable("vehicleId") String vehicleId,
            @PageableDefault(size = MaintenanceController.PAGE_SIZE, page = 0) Pageable pageable) {
        def maintenances = maintenancesService.getMaintenances(pageable, vehicleId)
        def maintenancesDTO = maintenances.content.collect { new MaintenanceDTO((it)) }

        return new PageImpl(maintenancesDTO, pageable, maintenances.totalElements)

    }
}
