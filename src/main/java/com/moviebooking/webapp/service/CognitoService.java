package com.moviebooking.webapp.service;

import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.moviebooking.webapp.domain.LoggedInUser;
import com.moviebooking.webapp.domain.LoginRequest;
import com.moviebooking.webapp.domain.RegisterationRequest;
import com.moviebooking.webapp.domain.UserChangePasswordRequest;
import com.moviebooking.webapp.domain.UserForgotPasswordRequest;
import com.moviebooking.webapp.domain.UserResetPasswordRequest;

public interface CognitoService {

	public String registerUser(RegisterationRequest user) throws UsernameExistsException;

	public LoggedInUser loginUser(LoginRequest loginRequest);

	public String refreshUserSession(String id, String idToken) throws Exception;

	public String forgotPassword(UserForgotPasswordRequest forgotPasswordRequest);

	public String resetPassword(UserResetPasswordRequest user);

	public Boolean changePassword(UserChangePasswordRequest changePasswordRequest);

	public Boolean logout();

}
