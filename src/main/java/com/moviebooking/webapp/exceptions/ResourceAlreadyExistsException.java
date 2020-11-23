package com.moviebooking.webapp.exceptions;

@SuppressWarnings("serial")
public class ResourceAlreadyExistsException extends RuntimeException {

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
