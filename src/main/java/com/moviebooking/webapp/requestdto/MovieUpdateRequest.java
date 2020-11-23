package com.moviebooking.webapp.requestdto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieUpdateRequest {

	@NotNull
	@NotEmpty
	private String movieId;
	private String movieName;
	@Range(min = 0l, max = 10l, message = "Rating must be between 0-10")
	private Double rating;

}
