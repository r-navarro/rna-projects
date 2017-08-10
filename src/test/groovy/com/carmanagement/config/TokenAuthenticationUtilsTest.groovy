package com.carmanagement.config

import com.carmanagement.config.jwt.TokenAuthenticationUtils
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Profile("test")
@Primary
@Component
class TokenAuthenticationUtilsTest implements TokenAuthenticationUtils {

    void addAuthentication(HttpServletResponse res, Authentication auth) {
        def claims = Jwts.claims().setSubject(auth.getName())
        claims.put(ROLES, auth.getAuthorities())
        def jwtToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact()
        res.addHeader(HEADER, TOKEN_PREFIX + ' ' + jwtToken)
    }

    Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER)
        if (token != null) {
            def jwtBody = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ''))
                    .getBody()
            def user = jwtBody.getSubject()
            def roles = jwtBody.get(ROLES)

            def grantedAuthorities = roles.collect {
                return new SimpleGrantedAuthority(it.authority)
            }

            return user ? new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities) : null
        }
        return null
    }
}
