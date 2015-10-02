package com.carmanagement.controller

import com.carmanagement.dto.UserDTO
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
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

import java.security.Principal

@RestController
@Secured("ROLE_USER")
@Slf4j
class UserController {

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Principal login(Principal user) {
        return user
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    def ResponseEntity<UserDTO> get(@PathVariable Long id) {
        if (checkUserRight(id)) {
            def user = userService.findById(id)
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK)
        }
        throw new SecurityException()
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    def ResponseEntity<UserDTO> getCurrent() {
        def authentication = SecurityContextHolder.getContext().authentication
        def userDto = new UserDTO(userService.findByName(authentication.name))
        return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK)
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    def ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        if (checkUserRight(id)) {
            return new ResponseEntity(userService.update(userDTO), HttpStatus.CREATED)
        }
        throw new SecurityException()
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    def ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return new ResponseEntity(userService.create(userDTO), HttpStatus.CREATED)
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    def void delete(@PathVariable Long id) {
        userService.delete(id)
    }


    @RequestMapping(value = "/users/all", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    def all() {

        def result = [SecurityContextHolder.getContext().authentication.name]
        userRepository.findAll().each {
            result << new UserDTO(it)
        }
        return result
    }

    private boolean checkUserRight(Long userId) {
        def user = userService.findById(userId)
        if (!user) {
            throw new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: userId)
        }
        def authentication = SecurityContextHolder.getContext().authentication
        if (user.name == authentication.name) {
            return true
        }
        return isAdmin(authentication)
    }

    private boolean isAdmin(Authentication authentication) {
        def auth = ((UserDetails) authentication.principal).authorities.find { it.authority == 'ROLE_ADMIN' }
        return auth != null
    }
}
