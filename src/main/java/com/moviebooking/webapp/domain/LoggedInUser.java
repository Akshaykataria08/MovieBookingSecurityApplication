package com.moviebooking.webapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInUser {

	private String email;
	private String phoneNumber;
	private String idToken;
}
