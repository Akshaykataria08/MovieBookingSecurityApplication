package com.moviebooking.webapp.exceptions;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
	
	public UnauthorizedException(String msg) {
		super(msg);
	}
}
