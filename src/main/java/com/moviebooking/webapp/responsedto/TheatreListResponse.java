package com.moviebooking.webapp.responsedto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreListResponse extends Response {

	private List<TheatreResponse> theatres;
}
