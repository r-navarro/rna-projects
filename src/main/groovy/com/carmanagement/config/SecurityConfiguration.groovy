package com.carmanagement.config

import com.carmanagement.services.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        //@formatter:off
        httpSecurity
                .httpBasic().and()
                .authorizeRequests()
                    .antMatchers("/vehicles/**").authenticated()
                    .antMatchers("/users/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter)
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and().logout().logoutSuccessUrl("/")
        //@formatter:on
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository()
        repository.headerName = "X-XSRF-TOKEN"
        return repository
    }
}
