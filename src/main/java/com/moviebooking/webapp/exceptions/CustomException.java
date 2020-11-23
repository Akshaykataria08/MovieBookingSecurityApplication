package com.moviebooking.webapp.exceptions;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

	public CustomException(String message) {
		super(message);
	}
}
