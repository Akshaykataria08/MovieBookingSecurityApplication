package com.moviebooking.webapp.service;

import org.springframework.http.ResponseEntity;

import com.moviebooking.webapp.domain.LoggedInUser;
import com.moviebooking.webapp.requestdto.UserAuthCredentials;
import com.moviebooking.webapp.requestdto.UserChangePasswordRequest;
import com.moviebooking.webapp.requestdto.UserForgotPasswordRequest;
import com.moviebooking.webapp.requestdto.UserProfileRegistrationRequest;
import com.moviebooking.webapp.requestdto.UserResetPasswordRequest;
import com.moviebooking.webapp.responsedto.Response;

import reactor.core.publisher.Mono;

public interface CognitoService {

	public Mono<ResponseEntity<? extends Response>> registerUser(UserAuthCredentials userAuthCredentials,
			UserProfileRegistrationRequest userProfileRegistrationRequest);

	public Mono<LoggedInUser> loginUser(UserAuthCredentials loginRequest);

	public String refreshUserSession(String id, String idToken) throws Exception;

	public String forgotPassword(UserForgotPasswordRequest forgotPasswordRequest);

	public String resetPassword(UserResetPasswordRequest user);

	public Boolean changePassword(UserChangePasswordRequest changePasswordRequest);

	public Boolean logout();
}
