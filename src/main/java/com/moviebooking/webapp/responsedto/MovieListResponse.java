package com.moviebooking.webapp.responsedto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieListResponse extends Response {

	private List<MovieResponse> movies = new ArrayList<MovieResponse>();
}
