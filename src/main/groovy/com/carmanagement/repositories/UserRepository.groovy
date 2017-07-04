package com.carmanagement.repositories

import com.carmanagement.entities.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {

    User findByName(String username)
}