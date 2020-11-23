package com.moviebooking.webapp.requestdto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieCreateRequest {

	@NotNull
	@NotEmpty
	private String movieName;
	
	@NotNull
	@Range(min = 0l, max = 10l, message = "Rating must be between 0-10")
	private Double rating;
	
	@NotNull
	@NotEmpty
	private List<String> cast = new ArrayList<String>();
	
	@NotNull
	@NotEmpty
	private String releaseDate;
	
	@NotNull
	@NotEmpty
	private String duration;
	
	@NotNull
	@NotEmpty
	private List<String> languages;
	
	@NotNull
	@NotEmpty
	private List<String> genres;
}
