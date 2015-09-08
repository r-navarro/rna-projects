package com.carmanagement.services.interfaces

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.TechnicalException
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService extends UserDetailsService {

    boolean checkUserVehicle(String userName, Vehicle vehicle)

    User findByName(String userName)

    UserDTO create(UserDTO userDTO) throws TechnicalException

    UserDTO update(UserDTO userDTO) throws TechnicalException

    void delete(UserDTO userDTO) throws TechnicalException
}