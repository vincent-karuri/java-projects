package com.example.spring.mvc.hibernate.mysql.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Created by Vincent Karuri on 08/08/2020
 */

@Controller
public class UserController {

	@GetMapping("/")
	public String index(Model model, Principal principal) {
		model.addAttribute("message", "You are logged in as " + principal.getName());
		return "index";
	}
}
