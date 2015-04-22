package com.carmanagement.config

import com.carmanagement.services.UserService
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
    UserService userService

    private String[] allowResources = ["/index.html", "/partials/login.html", "/login", "/", "/css/**", "/js/**", "/lib/**"]

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        http.authorizeRequests().antMatchers(allowResources).permitAll()
        http.authorizeRequests().anyRequest().fullyAuthenticated()
        http.httpBasic()
        http.csrf().disable()
    }
}
