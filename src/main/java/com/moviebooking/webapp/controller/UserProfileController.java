package com.moviebooking.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.exceptions.UnauthorizedException;
import com.moviebooking.webapp.requestdto.UserProfileUpdateRequest;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.UserProfileResponse;
import com.moviebooking.webapp.security.JwtAuthentication;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/profile")
public class UserProfileController {

	@Value("${userProfileUrl}")
	private String userProfileUrl;

	@Autowired
	private ResponseUtility responseUtility;

	@PutMapping
	public Mono<ResponseEntity<? extends Response>> updateUserProfile(
			@RequestBody UserProfileUpdateRequest userProfileUpdateRequest, HttpServletRequest request) {
		if(!this.getEmailFromSecurityContext().equalsIgnoreCase(userProfileUpdateRequest.getEmail()))
		{
			throw new UnauthorizedException("You are UnAuthorized");
		}
		return responseUtility.sendPutRequest(userProfileUrl, request.getRequestURI(), userProfileUpdateRequest,
				UserProfileUpdateRequest.class, UserProfileResponse.class);
	}

	@GetMapping
	public Mono<ResponseEntity<? extends Response>> getUserProfile(HttpServletRequest request) {
		
		return responseUtility.sendGetRequest(userProfileUrl, request.getRequestURI()+ "/" + this.getEmailFromSecurityContext(), UserProfileResponse.class);
	}
	
	private String getEmailFromSecurityContext() {
		return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getJwtClaimsSet()
				.getClaim(JwtConstants.EMAIL_CLIAM).toString();
	}
}
