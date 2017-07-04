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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

import java.security.Principal

@RestController
@Slf4j
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/users")
class UserController {

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    Principal login(Principal user) {
        return user
    }

    @RequestMapping(value = "/isAdmin", method = RequestMethod.GET)
    ResponseEntity<Boolean> isCurrentUserAdmin(Principal user) {
        def authentication = SecurityContextHolder.getContext().authentication

        def auth = ((UserDetails) authentication.principal).authorities.find { it.authority == 'ROLE_ADMIN' }
        return new ResponseEntity<Boolean>(auth ? Boolean.TRUE : Boolean.FALSE, HttpStatus.OK)
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<UserDTO> get(@PathVariable String id) {
        if (checkUserRight(id)) {
            def user = userService.findById(id)
            return new ResponseEntity(new UserDTO(user), HttpStatus.OK)
        }
        throw new SecurityException()
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "", method = RequestMethod.GET)
    ResponseEntity<List<UserDTO>> getCurrent() {
        def result = []
        userRepository.findAll().each {
            result << new UserDTO(it)
        }
        return new ResponseEntity(result, HttpStatus.OK)
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        if (checkUserRight(id)) {
            def user = userDTO.toUser()
            userDTO = new UserDTO(userService.save(user))
            return new ResponseEntity(userDTO, HttpStatus.CREATED)
        }
        throw new SecurityException()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return new ResponseEntity(userService.save(userDTO.toUser()), HttpStatus.CREATED)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id) {
        userService.delete(id)
    }

    private boolean checkUserRight(String userId) {
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

    private static boolean isAdmin(Authentication authentication) {
        def auth = ((UserDetails) authentication.principal).authorities.find { it.authority == 'ROLE_ADMIN' }
        return auth != null
    }
}
