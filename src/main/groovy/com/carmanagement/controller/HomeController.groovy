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
}
