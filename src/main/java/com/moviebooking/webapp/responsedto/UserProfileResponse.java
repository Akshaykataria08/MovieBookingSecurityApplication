package com.moviebooking.webapp.responsedto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileResponse extends Response {

	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String dob;
	private String city;
	private List<String> preferredLanguages = new ArrayList<>();
	private List<String> preferredGenres = new ArrayList<>();
}
