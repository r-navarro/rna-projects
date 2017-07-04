package com.carmanagement.config

import com.carmanagement.entities.User
import com.carmanagement.services.interfaces.UserService
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@ComponentScan("com.carmanagement")
@EnableWebMvc
class ApplicationConfig implements InitializingBean {

    @Autowired
    UserService userService

    @Override
    void afterPropertiesSet() throws Exception {
        if (!userService.findByName("admin")) {
            userService.save(new User(name: "admin", password: "admin"))
        }

        if (!userService.findByName("toto")) {
            userService.save(new User(name: "toto", password: "toto"))
        }
    }
}
