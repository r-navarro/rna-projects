package com.carmanagement.config.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JWTAuthenticationFilter extends GenericFilterBean {

    TokenAuthenticationUtils tokenAuthenticationUtils

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        def authentication = tokenAuthenticationUtils.getAuthentication((HttpServletRequest) request)
        SecurityContextHolder.getContext().setAuthentication(authentication)
        filterChain.doFilter(request, response)
    }
}