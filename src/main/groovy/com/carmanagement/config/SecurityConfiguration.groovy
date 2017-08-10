package com.carmanagement.config

import com.carmanagement.config.jwt.JWTAuthenticationFilter
import com.carmanagement.config.jwt.JWTLoginFilter
import com.carmanagement.config.jwt.TokenAuthenticationUtils
import com.carmanagement.services.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService

    @Autowired
    TokenAuthenticationUtils tokenAuthenticationUtils

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        //@formatter:off
        httpSecurity
                //.httpBasic().and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                //.and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter)
                //.csrf().csrfTokenRepository(csrfTokenRepository())
                .and().cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // We filter the api/login requests
                .and().addFilterBefore(jWTLoginFilter(), UsernamePasswordAuthenticationFilter)
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(jWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter)
                .csrf().disable()
        //@formatter:on
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository()
        repository.headerName = "X-XSRF-TOKEN"
        return repository
    }

    @Bean
    JWTLoginFilter jWTLoginFilter() {
        def jWTLoginFilter = new JWTLoginFilter("/login", authenticationManager())
        jWTLoginFilter.tokenAuthenticationUtils = tokenAuthenticationUtils
        return jWTLoginFilter
    }

    @Bean
    JWTAuthenticationFilter jWTAuthenticationFilter() {
        return new JWTAuthenticationFilter(tokenAuthenticationUtils: tokenAuthenticationUtils)
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration()
        configuration.setAllowedOrigins(['*'])
        configuration.setAllowedMethods(['HEAD', 'GET', 'POST', 'PUT', 'DELETE', 'PATCH'])
        configuration.setAllowCredentials(true)
        configuration.setMaxAge(3600)
        configuration.setAllowedHeaders(['Authorization', 'Cache-Control', 'Content-Type'])
        configuration.setExposedHeaders(['Authorization'])
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration('/**', configuration)
        return source
    }
}
