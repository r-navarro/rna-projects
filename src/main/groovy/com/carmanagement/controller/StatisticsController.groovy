package com.carmanagement.controller

import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
@Slf4j
class StatisticsController {

    @RequestMapping("/ko")
    String logKo(@RequestBody String report) {
        log.info "log ko ${report}"

        return '{ "message" : "log ko"}'
    }

    @RequestMapping("/ok")
    String logOK(@RequestBody String report) {
        //log.info "log ok ${report}"

        return '{ "message" : "log ok"}'
    }
}
