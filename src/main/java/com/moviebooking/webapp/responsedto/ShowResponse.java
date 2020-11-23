package com.moviebooking.webapp.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowResponse extends Response {

	private String showId;
	private String showStartTime;
	private String showEndTime;
	private String language;

}
