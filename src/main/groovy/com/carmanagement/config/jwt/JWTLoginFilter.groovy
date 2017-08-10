package com.carmanagement.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    TokenAuthenticationUtils tokenAuthenticationUtils

    JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url))
        setAuthenticationManager(authManager)
    }

    @Override
    Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, ServletException {

        try {
            def accountCredentials = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class)
            def token = new UsernamePasswordAuthenticationToken(
                    accountCredentials.userName,
                    accountCredentials.password,
                    []
            )
            return getAuthenticationManager().authenticate(token)
        } catch (emAll) {
            res.setStatus(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
            throws IOException, ServletException {
        tokenAuthenticationUtils.addAuthentication(res, auth)
    }


}