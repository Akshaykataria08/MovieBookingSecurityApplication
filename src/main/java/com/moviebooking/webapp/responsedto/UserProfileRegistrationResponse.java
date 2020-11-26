package com.moviebooking.webapp.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileRegistrationResponse extends Response {

	private String userId;
	private String dob;
	private String phoneNo;
	private String firstName;
	private String lastName;
}
