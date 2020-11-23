package com.moviebooking.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.TheatreCreateRequest;
import com.moviebooking.webapp.requestdto.TheatreUpdateRequest;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.TheatreResponse;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/theatre")
public class TheatreController {

	@Value("${movieUrl}")
	private String movieUrl;

	@Autowired
	private ResponseUtility responseUtility;
	
	@GetMapping("/{theatreId}")
	public Mono<ResponseEntity<? extends Response>> getTheatre(@PathVariable String theatreId, HttpServletRequest request) {
		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), TheatreResponse.class);
	}
	
	@PostMapping
	public Mono<ResponseEntity<? extends Response>> addTheatre(@Valid @RequestBody TheatreCreateRequest theatreRequest, HttpServletRequest request) {
		return responseUtility.sendPostRequest(movieUrl, request.getRequestURI(), theatreRequest, TheatreCreateRequest.class, TheatreResponse.class);
	}
	
	@PutMapping
	public Mono<ResponseEntity<? extends Response>> updateTheatre(@Valid @RequestBody TheatreUpdateRequest theatreRequest, HttpServletRequest request) {
		return responseUtility.sendPutRequest(movieUrl, request.getRequestURI(), theatreRequest, TheatreUpdateRequest.class, TheatreResponse.class);
	}
	
	@DeleteMapping("/{theatreId}")
	public Mono<ResponseEntity<? extends Response>> deleteTheatre(@PathVariable String theatreId, HttpServletRequest request) {
		return responseUtility.sendDeleteRequest(movieUrl, request.getRequestURI(), TheatreResponse.class);
	}
}
