package com.carmanagement.controller

import com.carmanagement.entities.User
import com.carmanagement.repositories.UserRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
class UserController {

    @Autowired
    UserRepository userRepository

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User user) {
        log.info("looking for ${user.username}")
        User databaseUser = userRepository.findByUsername(user.username)
        if (databaseUser && databaseUser.password == user.password) {
            return new ResponseEntity<String>(HttpStatus.OK)
        }
        return new ResponseEntity<String>("User not found", HttpStatus.UNAUTHORIZED)
    }
}
