package com.carmanagement.config

import com.carmanagement.entities.User
import com.carmanagement.repositories.UserRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan("com.carmanagement")
@EnableJpaRepositories("com.carmanagement.repositories")
class ApplicationConfig implements InitializingBean {

    @Autowired
    UserRepository userRepository

    @Override
    void afterPropertiesSet() throws Exception {
        if (!userRepository.findByName("admin")) {
            userRepository.save(new User(name: "admin", password: "admin"))
        }

        if (!userRepository.findByName("toto")) {
            userRepository.save(new User(name: "toto", password: "toto"))
        }
    }
}
