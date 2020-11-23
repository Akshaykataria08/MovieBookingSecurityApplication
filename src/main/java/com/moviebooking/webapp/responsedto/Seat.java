package com.moviebooking.webapp.responsedto;

import com.moviebooking.webapp.enums.SeatStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Seat {

	private double price;
	private SeatStatus status;
}
