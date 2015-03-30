package com.carmanagement.services

import com.carmanagement.entities.User
import com.carmanagement.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User as SpringUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
        if (!user) {
            throw new UsernameNotFoundException("User ${username} not found")
        }
        def userDetail = new SpringUser(username, user.password, getGrantedAuthorities(username))

        return userDetail
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
        Collection<? extends GrantedAuthority> authorities = [];

        if (username == "admin") {
            authorities << new SimpleGrantedAuthority("ROLE_ADMIN")
            authorities << new SimpleGrantedAuthority("ROLE_USER")
        } else {
            authorities << new SimpleGrantedAuthority("ROLE_USER")
        }
        return authorities
    }
}
