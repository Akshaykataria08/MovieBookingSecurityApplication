package com.moviebooking.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.webapp.requestdto.MovieCreateRequest;
import com.moviebooking.webapp.requestdto.MovieUpdateRequest;
import com.moviebooking.webapp.responsedto.MovieListResponse;
import com.moviebooking.webapp.responsedto.MovieResponse;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.ShowListResponse;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {

	@Value("${movieUrl}")
	private String movieUrl;

	@Autowired
	private ResponseUtility responseUtility;

	@PostMapping
	public Mono<ResponseEntity<? extends Response>> addMovie(@Valid @RequestBody MovieCreateRequest movieRequest,
			HttpServletRequest request) {
		
		return responseUtility.sendPostRequest(movieUrl, request.getRequestURI(), movieRequest,
				MovieCreateRequest.class, MovieResponse.class);
	}

	@GetMapping
	public Mono<ResponseEntity<? extends Response>> getAllMovies(HttpServletRequest request) {

		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), MovieListResponse.class);
	}

	@GetMapping("/{movieId}")
	public Mono<ResponseEntity<? extends Response>> getMovie(@PathVariable String movieId, HttpServletRequest request) {

		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), MovieResponse.class);
	}

	@PutMapping
	public Mono<ResponseEntity<? extends Response>> updateMovie(@Valid @RequestBody MovieUpdateRequest movieRequest,
			HttpServletRequest request) {

		return responseUtility.sendPutRequest(movieUrl, request.getRequestURI(), movieRequest, MovieUpdateRequest.class,
				MovieResponse.class);
	}

	@GetMapping("/{movieId}/theatre/{theatreId}/show")
	public Mono<ResponseEntity<? extends Response>> getAllShowsOfMovieInTheatre(@PathVariable String theatreId,
			@PathVariable String movieId, HttpServletRequest request) {

		return responseUtility.sendGetRequest(movieUrl, request.getRequestURI(), ShowListResponse.class);
	}
}
