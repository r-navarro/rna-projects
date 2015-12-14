package com.carmanagement.dto

import com.carmanagement.entities.User

class UserDTO {

    Long id

    String name

    String password

    UserDTO() {
    }

    UserDTO(User user) {
        this.id = user.id
        this.name = user.name
    }

    User toUser() {
        def user = new User()
        user.id = this.id
        user.name = this.name
        user.password = this.password
        return user
    }
}
