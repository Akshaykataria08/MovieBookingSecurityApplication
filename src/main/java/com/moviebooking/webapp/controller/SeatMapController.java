package com.moviebooking.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.SeatMap;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/seatMap/{showId}")
public class SeatMapController {

	@Value("${bookingUrl}")
	private String bookingUrl;

	@Autowired
	private ResponseUtility responseUtility;
	
	@GetMapping
	public Mono<ResponseEntity<? extends Response>> getSeatMap(@PathVariable String showId, HttpServletRequest request) {
		return responseUtility.sendGetRequest(bookingUrl, request.getRequestURI(), SeatMap.class);
	}
}
