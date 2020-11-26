package com.moviebooking.webapp.domain;

import com.moviebooking.webapp.responsedto.UserProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInUser {

	private UserProfileResponse userProfileResponse;
	private String idToken;
}
