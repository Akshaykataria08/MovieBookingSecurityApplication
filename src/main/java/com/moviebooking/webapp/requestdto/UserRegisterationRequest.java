package com.moviebooking.webapp.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterationRequest {

	private String email;
	private String dob;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String password;
}
