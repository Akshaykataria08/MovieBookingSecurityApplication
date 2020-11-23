package com.moviebooking.webapp.exceptions;

@SuppressWarnings("serial")
public class InternalServerErrorException extends RuntimeException{

	public InternalServerErrorException(String msg) {
		super(msg);
	}
}
