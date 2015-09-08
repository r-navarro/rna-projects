package com.carmanagement.config

import com.carmanagement.services.impls.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserServiceImpl userService

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        //@formatter:off
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/vehicles/**").authenticated()
                    .antMatchers("/users/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .httpBasic()
        //@formatter:on
    }
}
