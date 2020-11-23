package com.moviebooking.webapp.controller;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.LocationCreateRequest;
import com.moviebooking.webapp.responsedto.LocationListResponse;
import com.moviebooking.webapp.responsedto.LocationResponse;
import com.moviebooking.webapp.responsedto.MovieListResponse;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.ShowListResponse;
import com.moviebooking.webapp.responsedto.TheatreListResponse;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/location")
public class LocationController {

	@Value("${movieUrl}")
	private String movieUrl;

	@Autowired
	private ResponseUtility responseUtility;

	@GetMapping
	public Mono<ResponseEntity<? extends Response>> getAllLocations(HttpServletRequest request)
			throws URISyntaxException {

		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), LocationListResponse.class);
	}

	@PostMapping
	public Mono<ResponseEntity<? extends Response>> addLocation(
			@Valid @RequestBody LocationCreateRequest locationRequest, HttpServletRequest request)
			throws URISyntaxException {

		return responseUtility.sendPostRequest(movieUrl, request.getRequestURI(), locationRequest,
				LocationCreateRequest.class, LocationResponse.class);

	}

	@DeleteMapping("/{locationId}")
	public Mono<ResponseEntity<? extends Response>> deleteLocation(@PathVariable String locationId,
			HttpServletRequest request) {

		return responseUtility.sendDeleteRequest(movieUrl, request.getRequestURI(), LocationResponse.class);
	}

	@DeleteMapping("/{locationId}/movie/{movieId}")
	public Mono<ResponseEntity<? extends Response>> deleteShowsOfMovieInLocation(@PathVariable String locationId,
			@PathVariable String movieId, HttpServletRequest request) {

		return responseUtility.sendDeleteRequest(movieUrl, request.getRequestURI(), ShowListResponse.class);
	}

	@GetMapping("/{locationId}/movie")
	public Mono<ResponseEntity<? extends Response>> getAllMoviesInCity(@PathVariable String locationId,
			@RequestParam(required = false) Double rating, HttpServletRequest request) {
		return responseUtility.sendGetRequest(movieUrl,
				request.getRequestURI() + "?" + StringUtils.trimToEmpty(request.getQueryString()), MovieListResponse.class);

	}

	@GetMapping("/{locationId}/theatre")
	public Mono<ResponseEntity<? extends Response>> getAllTheatreInLocation(@PathVariable String locationId,
			HttpServletRequest request) {
		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), TheatreListResponse.class);
	}
}
