package com.carmanagement.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@ComponentScan("com.carmanagement")
@Profile("test")
class PersistenceTestConfig {

}
