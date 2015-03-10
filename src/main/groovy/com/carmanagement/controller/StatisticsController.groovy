package com.carmanagement.controller

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class StatisticsController {

	@RequestMapping("/ko")
	def String logKo(@RequestBody String repport){
		println "log ko ${repport}"

		return '{ "message" : "log ko"}'
	}

	@RequestMapping("/ok")
	def String logOK(@RequestBody String repport){
		println "log ok ${repport}"

		return '{ "message" : "log ok"}'
	}
}
