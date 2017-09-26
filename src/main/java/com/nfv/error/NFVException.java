package com.nfv.error;

import org.springframework.http.HttpStatus;

public class NFVException extends Exception {

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	
	public NFVException(String errorMessage, HttpStatus httpStatus) {
		super (errorMessage);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
