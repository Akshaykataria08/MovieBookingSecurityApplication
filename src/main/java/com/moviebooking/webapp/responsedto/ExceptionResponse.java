package com.moviebooking.webapp.responsedto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse extends Response {

	private String errorMessage;
	private String errorCode;
	private LocalDateTime timestamp;
	
	public ExceptionResponse(String errorMessage, String errorCode, LocalDateTime timestamp) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.timestamp = timestamp;
	}
}
