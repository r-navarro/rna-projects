package com.carmanagement.services.interfaces

import com.carmanagement.dto.VehicleDTO
import com.carmanagement.entities.User
import com.carmanagement.exceptions.TechnicalException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface VehiclesService {

    VehicleDTO save(VehicleDTO vehicleDTO, User user)

    Page<VehicleDTO> getVehicles(Pageable pageable, String name)

    VehicleDTO get(Long id, String name)

    void delete(Long id) throws TechnicalException

    List<VehicleDTO> findAll()
}