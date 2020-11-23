package com.moviebooking.webapp.requestdto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.moviebooking.webapp.domain.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TheatreCreateRequest {

	@NotNull
	@NotEmpty
	private String theatreName;

	@NotNull
	@Range(min = 0l, max = 5l, message = "Rating must be between 0-5")
	private Double rating;
	
	@NotNull
	@Valid
	private Address address;
}
