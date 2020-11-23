package com.moviebooking.webapp.responsedto;

import com.moviebooking.webapp.domain.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TheatreResponse extends Response {

	private String theatreId;
	private String theatreName;
	private Double rating;
	private Address address;
}
