package com.demo.exception;

import com.demo.util.AppSupportFunctions;

/**
 * This interface is meant to be implemented by enums 
 * that are going to define error codes and messages.  
 * 
 * @author Niranjan Nanda
 */
public interface ExceptionEnum {
	/**
	 * Returns the exception code associated with error enum
	 * 
	 * @return	The exception code.
	 */
	String exceptionCode();
	
	/**
	 * Returns the exception message associated with error enum
	 * 
	 * @return	The exception message.
	 */
	String exceptionMessage();
	
	/**
	 * Combines both exception code and message and forms a printable string.
	 * 
	 * @return	A string by combining exception code and message. 
	 */
	default String toMessageString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(exceptionCode());
		builder.append(": ");
		builder.append(exceptionMessage());
		return builder.toString();
	}
	
	/**
	 * Formats the exception message with the help of args.
	 * 
	 * @param messageArgs	The message arguments which will be replaced in the exception 
	 * message.
	 * @return	Formatted exception message.
	 */
	default String toFormattedExceptionMessage(final String[] messageArgs) {
		return AppSupportFunctions.FN_FORMAT_STRING.apply(exceptionMessage(), messageArgs);
	}
}
