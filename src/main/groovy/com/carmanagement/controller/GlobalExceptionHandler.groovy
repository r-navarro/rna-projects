package com.carmanagement.controller

import com.carmanagement.controller.pojo.ErrorResponse
import com.carmanagement.exceptions.SecurityException
import com.carmanagement.exceptions.TechnicalException
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    @ExceptionHandler(TechnicalException)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponse handleTechnicalException(TechnicalException e, HttpServletRequest request) {
        log.info(e.getMessage())
        def requestUrl = request.getPathInfo() ?: request.getServletPath()
        return new ErrorResponse(errorMessage: e.getMessage(), request: requestUrl, method: request.getMethod())
    }

    @ExceptionHandler(SecurityException)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    ErrorResponse handleSecurityException(SecurityException e, HttpServletRequest request) {
        log.info(e.getMessage())
        def requestUrl = request.getPathInfo() ?: request.getServletPath()
        return new ErrorResponse(errorMessage: e.getMessage(), request: requestUrl, method: request.getMethod())
    }
}
