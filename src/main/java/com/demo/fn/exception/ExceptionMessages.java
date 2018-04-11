package com.demo.fn.exception;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public final class ExceptionMessages {
	private ExceptionMessages() {}
	
	public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong while processing your request. Please contact PDP Support Team.";
	
	public static final String REST_400001 = "Required Http Header '%s' is missing.";
	public static final String REST_400002 = "Required header '%s' has unacceptable value in the request.";
	public static final String REST_400003 = "Resource ID and name cannot be determined from request path '%s'";
	public static final String REST_400004 = "Resource ID must be provided request path '%s' for '%s' operation.";
	
	public static final String REST_401001 = "App authentication failed.";
	
	public static final String REST_404001 = "Resource with id '%s' was not found.";
	
	
	
}
