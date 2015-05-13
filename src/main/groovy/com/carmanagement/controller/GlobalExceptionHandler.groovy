package com.carmanagement.controller

import com.carmanagement.exceptions.TechnicalException
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    @ExceptionHandler(TechnicalException)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Technical exception")
    public void handleTechnicalException(TechnicalException e) {
        log.info(e.getMessage())
    }
}
