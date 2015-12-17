package com.carmanagement.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @RequestMapping("/home")
    @Secured("ROLE_ADMIN")
    public String home() {
        return "index"
    }

    @RequestMapping("/isAlive")
    public String isAlive() {
        def builder = new StringBuilder()
        builder.with {
            append System.currentTimeMillis()
            append " / "
            append System.getProperty("os.name")
        }
        return builder.toString()
    }
}
