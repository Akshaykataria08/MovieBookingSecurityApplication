package com.moviebooking.webapp.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthCredentials {

	private String email;
	private String phoneNumber;
	private String password;
}
