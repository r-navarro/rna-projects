package com.carmanagement.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such user")
class UserNotFoundException extends RuntimeException {

    UserNotFoundException(long id) {
        super("No such user with id ${id}")
    }
}
