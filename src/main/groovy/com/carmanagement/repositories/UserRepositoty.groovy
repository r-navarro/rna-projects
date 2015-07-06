package com.carmanagement.repositories

import com.carmanagement.entities.User
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByName(String username)
}