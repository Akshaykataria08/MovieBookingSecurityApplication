package com.moviebooking.webapp.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.domain.LoggedInUser;
import com.moviebooking.webapp.domain.LoginRequest;
import com.moviebooking.webapp.domain.RegisterationRequest;
import com.moviebooking.webapp.domain.User;
import com.moviebooking.webapp.domain.UserChangePasswordRequest;
import com.moviebooking.webapp.domain.UserForgotPasswordRequest;
import com.moviebooking.webapp.domain.UserResetPasswordRequest;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.service.CognitoService;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class CognitoController {

	@Autowired
	private ModelMapper dtoMapper;

	@Autowired
	private CognitoService cognitoService;

	@PostMapping("/register")
	public String registerUser(@RequestBody RegisterationRequest user) {
		return cognitoService.registerUser(user);
	}

	@PostMapping("/login")
	public User loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		LoggedInUser loggedInUser = cognitoService.loginUser(loginRequest);
		Cookie cookie = new Cookie(JwtConstants.AUTH_COOKIE_NAME, loggedInUser.getIdToken());
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return dtoMapper.map(loggedInUser, User.class);
	}

	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestBody UserForgotPasswordRequest userForgotPasswordRequest) {
		return cognitoService.forgotPassword(userForgotPasswordRequest);
	}

	@PutMapping("/resetPassword")
	public String resetPassword(@RequestBody UserResetPasswordRequest resetPasswordRequest) {
		return cognitoService.resetPassword(resetPasswordRequest);
	}

	@PutMapping("/changePassword")
	public Boolean changePassword(@RequestBody UserChangePasswordRequest changePasswordRequest) {
		return cognitoService.changePassword(changePasswordRequest);
	}
	
	@GetMapping("/logout")
	public Boolean logout(HttpServletResponse response) {
		Boolean flag = cognitoService.logout();
		Cookie cookie = new Cookie(JwtConstants.AUTH_COOKIE_NAME, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return flag;
	}
}
