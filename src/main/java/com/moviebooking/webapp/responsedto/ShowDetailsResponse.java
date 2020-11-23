package com.moviebooking.webapp.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowDetailsResponse extends Response {

	private String showStartTime;
	private String showEndTime;
	private String movieName;
	private String theatreName;
	private String city;
	private String state;
}
