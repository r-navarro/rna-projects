package com.carmanagement.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such vehicle")
class VehicleNotFoundException extends RuntimeException {

    VehicleNotFoundException(long id) {
        super("No such vehicle with id ${id}")
    }
}
