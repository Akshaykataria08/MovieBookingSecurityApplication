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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.ShowRequest;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.ShowDetailsResponse;
import com.moviebooking.webapp.responsedto.ShowResponse;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/show")
public class ShowController {

	@Value("${movieUrl}")
	private String movieUrl;

	@Autowired
	private ResponseUtility responseUtility;
	
	@DeleteMapping("/{showId}")
	public Mono<ResponseEntity<? extends Response>> removeShow(@PathVariable String showId, HttpServletRequest request) {
		return responseUtility.sendDeleteRequest(movieUrl, request.getRequestURI(), ShowResponse.class);
	}
	
	@PostMapping
	public Mono<ResponseEntity<? extends Response>> addShow(@Valid @RequestBody ShowRequest showRequest, HttpServletRequest request) {
		return responseUtility.sendPostRequest(movieUrl, request.getRequestURI(), showRequest, ShowRequest.class, ShowResponse.class);
	}
	
	@GetMapping("/info/{showId}")
	public Mono<ResponseEntity<? extends Response>> getShowDetails(@PathVariable String showId, HttpServletRequest request) {
		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), ShowDetailsResponse.class);
	}
}
