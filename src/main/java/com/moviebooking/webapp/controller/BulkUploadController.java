package com.moviebooking.webapp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moviebooking.webapp.responsedto.LocationListResponse;
import com.moviebooking.webapp.responsedto.MovieListResponse;
import com.moviebooking.webapp.responsedto.Response;
import com.moviebooking.webapp.responsedto.ShowListResponse;
import com.moviebooking.webapp.responsedto.TheatreListResponse;
import com.moviebooking.webapp.utility.ResponseUtility;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/upload/csv")
public class BulkUploadController {

	@Value("${movieUrl}")
	private String movieUrl;

	@Autowired
	private ResponseUtility responseUtility;

	@PostMapping("/location")
	public Mono<ResponseEntity<? extends Response>> uploadLocations(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		return responseUtility.uploadFile(movieUrl, request.getRequestURI(), file, LocationListResponse.class);
	}

	@PostMapping("/theatre")
	public Mono<ResponseEntity<? extends Response>> uploadTheatreCSV(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		return responseUtility.uploadFile(movieUrl, request.getRequestURI(), file, TheatreListResponse.class);
	}

	@PostMapping("/movie")
	public Mono<ResponseEntity<? extends Response>> uploadMovieCSV(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		return responseUtility.uploadFile(movieUrl, request.getRequestURI(), file, MovieListResponse.class);
	}

	@PostMapping("/show")
	public Mono<ResponseEntity<? extends Response>> uploadShowCSV(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		return responseUtility.uploadFile(movieUrl, request.getRequestURI(), file, ShowListResponse.class);
	}
}
