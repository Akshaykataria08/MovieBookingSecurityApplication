package com.moviebooking.webapp.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileRegistrationRequest {

	private String email;
	private String dob;
	private String phoneNumber;
	private String firstName;
	private String lastName;
}
