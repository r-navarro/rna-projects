package com.carmanagement.repositories

import com.carmanagement.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String username)
}