package com.moviebooking.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.BookingRequest;
import com.moviebooking.webapp.responsedto.BookingListResponse;
import com.moviebooking.webapp.responsedto.BookingResponse;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.security.JwtAuthentication;
import com.moviebooking.webapp.security.jwt.JwtConstants;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/booking")
public class BookingController {

	@Value("${bookingUrl}")
	private String bookingUrl;

	@Autowired
	private ResponseUtility responseUtility;

	@PostMapping
	public Mono<ResponseEntity<? extends Response>> bookSeats(@RequestBody BookingRequest bookingRequest,
			HttpServletRequest request) {

		bookingRequest.setUserId(this.getEmailFromSecurityContext());
		return responseUtility.sendPostRequest(bookingUrl, request.getRequestURI(), bookingRequest,
				BookingRequest.class, BookingResponse.class);
	}

	@GetMapping
	public Mono<ResponseEntity<? extends Response>> getAllUserBookings(HttpServletRequest request) {

		return responseUtility.sendGetRequest(bookingUrl,
				String.format(request.getRequestURI() + "/user/%s", this.getEmailFromSecurityContext()),
				BookingListResponse.class);
	}

	@PutMapping("/{bookingId}/cancel")
	public Mono<ResponseEntity<? extends Response>> cancelBooking(@PathVariable String bookingId,
			HttpServletRequest request) {

		return responseUtility.sendPutRequest(bookingUrl,
				request.getRequestURI() + "/" + this.getEmailFromSecurityContext(), BookingResponse.class);
	}

	private String getEmailFromSecurityContext() {

		return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getJwtClaimsSet()
				.getClaim(JwtConstants.EMAIL_CLIAM).toString();
	}
}
