package com.moviebooking.webapp.requestdto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

	private String showId;
	private String userId;
	private List<String> seatNos = new ArrayList<String>();
	
}
