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
public class LocationListResponse extends Response {
	
	private List<LocationResponse> locations = new ArrayList<LocationResponse>();
}
