package com.orenn.coupons.exceptions;

import com.orenn.coupons.enums.ErrorType;

public class ApplicationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorType errorType;
	
	public ApplicationException() {
	}
	
	public ApplicationException(String message) {
		super(message);
	}
	
	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}
	
	public ApplicationException(Exception e, ErrorType errorType, String message) {
		super(message, e);
		this.errorType = errorType;
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}

}
