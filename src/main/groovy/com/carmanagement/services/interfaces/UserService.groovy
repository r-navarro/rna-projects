package com.carmanagement.services.interfaces

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.Vehicle
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService extends UserDetailsService {

    boolean checkUserVehicle(String userName, Vehicle vehicle)

    UserDTO findByName(String userName)
}