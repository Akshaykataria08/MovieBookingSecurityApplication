package com.moviebooking.webapp.responsedto;

import java.util.ArrayList;
import java.util.List;

import com.moviebooking.webapp.enums.BookingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BookingResponse extends Response {

	private String bookingId;
	private String transactionId;
	private String bookingTime;
	private List<String> seatNos = new ArrayList<String>();
	private String showStartTime;
	private String showEndTime;
	private String movieName;
	private String theatreName;
	private String city;
	private String state;
	private double totalPrice;
	private BookingStatus bookingStatus;
	private String userId;
}
