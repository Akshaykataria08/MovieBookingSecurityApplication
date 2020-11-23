package com.moviebooking.webapp.requestdto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowRequest {

	@NotNull
	@NotEmpty
	private String showStartTime;
	
	@NotNull
	@NotEmpty
	private String showEndTime;
	
	@NotNull
	@NotEmpty
	private String movieId;
	
	@NotNull
	@NotEmpty
	private String language;
	
	@NotNull
	@NotEmpty
	private String theatreId;
}
