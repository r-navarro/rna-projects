package com.carmanagement.controller

import com.carmanagement.dto.UserDTO
import com.carmanagement.entities.User
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.SecurityException
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.services.interfaces.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured("ROLE_USER")
@Slf4j
class UserController {

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody User user) {
        log.info("looking for ${user.name}")
        User databaseUser = userRepository.findByName(user.name)
        if (databaseUser && databaseUser.password == user.password) {
            return databaseUser
        }
        return null
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    def ResponseEntity<UserDTO> get(@PathVariable Long id) {
        def user = userRepository.findOne(id)
        if(!user) throw new TechnicalException(errorCode:ErrorCode.USER_NOT_FOUND, errorParameter: id)
        def auth = SecurityContextHolder.getContext().authentication
        if(auth.name == user.name) {
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK)
        } else if (auth.authorities.find {it.getAuthority() == 'ROLE_ADMIN'}) {
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK)
        }
        throw new SecurityException()
    }
}
