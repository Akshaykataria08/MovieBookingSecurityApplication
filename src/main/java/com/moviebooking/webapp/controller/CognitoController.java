package com.moviebooking.webapp.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.UserAuthCredentials;
import com.moviebooking.webapp.requestdto.UserChangePasswordRequest;
import com.moviebooking.webapp.requestdto.UserForgotPasswordRequest;
import com.moviebooking.webapp.requestdto.UserProfileRegistrationRequest;
import com.moviebooking.webapp.requestdto.UserRegisterationRequest;
import com.moviebooking.webapp.requestdto.UserResetPasswordRequest;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.UserProfileResponse;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.service.CognitoService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class CognitoController {

	@Autowired
	private ModelMapper dtoMapper;

	@Autowired
	private CognitoService cognitoService;

	@PostMapping("/register")
	public Mono<ResponseEntity<? extends Response>> registerUser(
			@RequestBody UserRegisterationRequest userRegisterationRequest) {
		UserAuthCredentials userAuthCredentials = dtoMapper.map(userRegisterationRequest, UserAuthCredentials.class);
		UserProfileRegistrationRequest userProfileRegistrationRequest = dtoMapper.map(userRegisterationRequest,
				UserProfileRegistrationRequest.class);
		return cognitoService.registerUser(userAuthCredentials, userProfileRegistrationRequest);
	}

	@PostMapping("/login")
	public Mono<UserProfileResponse> loginUser(@RequestBody UserAuthCredentials loginRequest, HttpServletResponse response) {
		return cognitoService.loginUser(loginRequest).flatMap(loggedInUser -> {
			Cookie cookie = new Cookie(JwtConstants.AUTH_COOKIE_NAME, loggedInUser.getIdToken());
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			return Mono.just(loggedInUser.getUserProfileResponse());
		});
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
