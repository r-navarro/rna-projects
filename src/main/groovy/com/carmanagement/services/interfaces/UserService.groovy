package com.carmanagement.services.interfaces

import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.TechnicalException
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService extends UserDetailsService {

    boolean checkUserVehicle(String userName, Vehicle vehicle)

    User findByName(String userName)

    User findById(Long id)

    User save(User user) throws TechnicalException

    void delete(Long id) throws TechnicalException

    List<User> findAll()
}