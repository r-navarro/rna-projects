package com.carmanagement.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {

    @Id
    String id

    @Indexed(unique = true)
    String name


    String password


    boolean enabled = true


    boolean accountExpired


    boolean accountLocked


    boolean passwordExpired

    List<Role> roles = []

    String toString() {
        return name
    }

}
