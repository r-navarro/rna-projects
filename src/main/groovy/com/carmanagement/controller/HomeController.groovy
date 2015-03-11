package com.carmanagement.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController {

	@RequestMapping("/home")
	public String home() {
		return "index"
	}
}
