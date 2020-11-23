package com.moviebooking.webapp.responsedto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieResponse extends Response {

	private String movieId;
	private String movieName;
	private Double rating;
	private List<String> cast = new ArrayList<String>();
	private String releaseDate;
	private String duration;
	private List<String> languages = new ArrayList<>();
	private List<String> genres = new ArrayList<>();
}
