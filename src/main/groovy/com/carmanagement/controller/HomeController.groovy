package com.carmanagement.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import java.security.Principal

@RestController
class HomeController {

    @RequestMapping("/home")
    @Secured("ROLE_ADMIN")
    String home() {
        return "index"
    }

    @RequestMapping("/isAlive")
    String isAlive() {
        def builder = new StringBuilder()
        builder.with {
            append System.currentTimeMillis()
            append " / "
            append System.getProperty("os.name")
        }
        return builder.toString()
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    Principal login(Principal user) {
        return user
    }
}
