package com.moviebooking.webapp.responsedto;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatMap extends Response {

	private String showId;
	private Map<String, Seat> seats = new HashMap<>();
	
	public SeatMap(String showId) {
		super();
		this.showId = showId;
	}
}