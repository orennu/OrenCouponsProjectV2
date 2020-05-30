package com.orenn.coupons.enums;

public enum ErrorType {
	
	GENERAL_ERROR(500, "GENERAL_ERROR", "General error", true),
	CREATE_ERROR(500, "CREATE_ERROR", "Error during SQL create operation", true),
	UPDATE_ERROR(500, "UPDATE_ERROR", "Error during SQL update operation", true),
	QUERY_ERROR(500, "QUERY_ERROR", "Error during SQL query operation", true),
	DELETE_ERROR(500, "DELETE_ERROR", "Error during SQL delete operation", true),
	NULL_ERROR(500, "NULL_ERROR", "Got null object", true),
	FORBIDDEN_ERROR(403, "FORBIDDEN_ERROR", "The operation is forbidden", false),
	ALREADY_EXISTS_ERROR(600, "ALREADY_EXISTS_ERROR", "already exists", false),
	NOT_EXISTS_ERROR(600, "NOT_EXISTS_ERROR", "does not exist", true),
	INVALID_LENGTH_ERROR(600, "INVALID_LENGTH_ERROR", "Invalid length", false),
	INVALID_CHARS_ERROR(600, "INVALID_CHARS_ERROR", "Invalid characters", false),
	INVALID_WHITESPACE_ERROR(600, "INVALID_WHITESPACE_ERROR", "cannot begin and/or end with whitespace or contain only whitespace", false),
	INVALID_PRICE_ERROR(600, "INVALID_PRICE_ERROR", "Invalid price, must be greater than 0", false),
	INVALID_QUANTITY_ERROR(600, "INVALID_QUANTITY_ERROR", "Invalid quantity", false),
	INVALID_FORMAT_ERROR(600, "INVALID_FORMAT_ERROR", "Invalid format", false),
	INVALID_DATE_ERROR(600, "INVALID_DATE_ERROR", "Invalid date", false),
	FORBIDDEN_TYPE(403, "FORBIDDEN_TYPE", "Forbidden type", false),
	EXCEEDED_ERROR(600, "EXCEEDED_ERROR", "", false),
	EMPTY_ERROR(600, "EMPTY_ERROR", "cannot be empty", false),
	LOGIN_FAILED(600, "LOGIN_FAILED", "Login failed, wrong username or password", false),
	
	;
	// add more errors
	
	private int errorCode;
	private String errorName;
	private String errorDescription;
	private boolean isPrintStackTrace;
	
	private ErrorType(int errorCode, String errorName, String errorDescription, boolean isPrintStackTrace) {
		this.errorCode = errorCode;
		this.errorName = errorName;
		this.errorDescription = errorDescription;
		this.isPrintStackTrace = isPrintStackTrace;
	}
	
	public int geterrorCode() {
		return errorCode;
	}
	
	public String getErrorName() {
		return errorName;
	}
	
	public String getErrorDescription() {
		return errorDescription;
	}
	
	public boolean getIsPrintStackTrace() {
		return isPrintStackTrace;
	}

}
