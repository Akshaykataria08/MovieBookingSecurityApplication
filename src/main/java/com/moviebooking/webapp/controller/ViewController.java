package com.moviebooking.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	
	@GetMapping("/")
	public String getHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String getRegisterationPage() {
		return "register";
	}
}
