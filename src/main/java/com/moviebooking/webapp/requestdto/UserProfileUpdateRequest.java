package com.moviebooking.webapp.requestdto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateRequest {

	private String email;
	private String phoneNumber;
	private String city;
	private List<String> preferredLanguages = new ArrayList<>();
	private List<String> preferredGenres = new ArrayList<>();
}
