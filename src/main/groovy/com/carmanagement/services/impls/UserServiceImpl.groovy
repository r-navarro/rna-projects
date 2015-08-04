package com.carmanagement.services.impls

import com.carmanagement.entities.User
import com.carmanagement.entities.Vehicle
import com.carmanagement.exceptions.ErrorCode
import com.carmanagement.exceptions.TechnicalException
import com.carmanagement.repositories.UserRepository
import com.carmanagement.services.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User as SpringUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
        if (!user) {
            throw new UsernameNotFoundException("User ${username} not found")
        }
        def userDetail = new SpringUser(username, user.password, getGrantedAuthorities(username))

        return userDetail
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
        Collection<? extends GrantedAuthority> authorities = []

        if (username == "admin") {
            authorities << new SimpleGrantedAuthority("ROLE_ADMIN")
            authorities << new SimpleGrantedAuthority("ROLE_USER")
        } else {
            authorities << new SimpleGrantedAuthority("ROLE_USER")
        }
        return authorities
    }

    @Override
    boolean checkUserVehicle(String userName, Vehicle vehicle) {
        def user = userRepository.findByName(userName)
        if (!user) throw new TechnicalException(errorCode: ErrorCode.USER_NOT_FOUND, errorParameter: userName)
        if (vehicle?.user?.name == user?.name) {
            return true
        } else {
            return false
        }
    }

    @Override
    User findByName(String userName) {
        return userRepository.findByName(userName)
    }
}
