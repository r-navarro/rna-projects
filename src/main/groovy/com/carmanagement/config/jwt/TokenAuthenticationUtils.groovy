package com.carmanagement.config.jwt

import org.springframework.security.core.Authentication

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface TokenAuthenticationUtils {

    static final long EXPIRATION_TIME = 864_000_000 // 10 days
    static final String SECRET = 'ThisIsASecret'
    static final String TOKEN_PREFIX = 'Bearer'
    static final String HEADER = 'Authorization'
    static final String ROLES = 'roles'

    void addAuthentication(HttpServletResponse res, Authentication auth)

    Authentication getAuthentication(HttpServletRequest request)
}