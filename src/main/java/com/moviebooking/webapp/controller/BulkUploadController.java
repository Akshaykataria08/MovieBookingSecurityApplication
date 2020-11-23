package com.moviebooking.webapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/upload/csv")
public class BulkUploadController {

	@Value("${movieUrl}")
	private String movieUrl;

//	@Autowired
//	private ResponseUtility responseUtility;
//	
//	@PostMapping("/location")
//	public boolean uploadLocations(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//		request.gethe
//	}
//	
//	@PostMapping("/theatre")
//	public boolean uploadTheatreCSV(@RequestParam("file") MultipartFile file) throws IOException {
//		
//	}
//	
//	@PostMapping("/movie")
//	public boolean uploadMovieCSV(@RequestParam("file") MultipartFile file) throws IOException {
//		
//	}
//	
//	@PostMapping("/show")
//	public boolean uploadShowCSV(@RequestParam("file") MultipartFile file) throws IOException {
//		
//	}
}
