package com.carmanagement.controller

import com.carmanagement.entities.User
import com.carmanagement.repositories.UserRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
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
    public User login(@RequestBody User user) {
        log.info("looking for ${user.name}")
        User databaseUser = userRepository.findByName(user.name)
        if (databaseUser && databaseUser.password == user.password) {
            return databaseUser
        }
        return null
    }
}
