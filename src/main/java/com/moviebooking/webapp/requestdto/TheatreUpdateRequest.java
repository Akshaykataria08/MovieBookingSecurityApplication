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
public class TheatreUpdateRequest {

	@NotNull
	@NotEmpty
	private String theatreId; 
	private String theatreName;
	@Range(min = 0l, max = 5l, message = "Rating must be between 0-5")
	private Double rating;
}
