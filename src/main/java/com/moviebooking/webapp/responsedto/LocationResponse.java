package com.moviebooking.webapp.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationResponse extends Response {

	private String locationId;
	private String city;
	private String state;

}
