package com.carmanagement.config.jwt

import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class JWTAuthenticationFilter extends GenericFilterBean {

    TokenAuthenticationUtils tokenAuthenticationUtils

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            def authentication = tokenAuthenticationUtils.getAuthentication((HttpServletRequest) request)
            SecurityContextHolder.getContext().setAuthentication(authentication)
            filterChain.doFilter(request, response)
        } catch (emAll) {
            log.warn("Logging fail", emAll)
            def httpServletResponse = response as HttpServletResponse
            httpServletResponse.setContentLength(0)
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value())
        }
    }
}