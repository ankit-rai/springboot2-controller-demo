package com.demo.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

/**
 * A utility class that provides {@link Function}s to aid
 * logging functionality.
 * 
 * @author Niranjan Nanda
 */
public final class LoggerUtilFunctions {
	private LoggerUtilFunctions() {}
	
	public static final BiFunction<String, String, String> FN_TX_PATH_BUILDER = (className, methodName) -> 
		new StringBuilder()
			.append(className)
			.append(LoggerConstants.TX_PATH_DELIMITER)
			.append(methodName)
			.toString();
	
	public static final Function<Object, String> FN_KV_LOG_ENTRY_SANITIZER = logEntry -> 
		StringUtils.replace(
			Objects.toString(logEntry), 
			LoggerConstants.DOUBLE_QUOTE, 
			LoggerConstants.DOUBLE_QUOTE_ESCAPE_STRING);
		
	public static final BiFunction<Throwable, Boolean, String> FN_THROWABLE_TO_MESSAGE_STRING = (throwable, isDebugEnabled) -> {
		if (isDebugEnabled) {
			final StringWriter stackTraceWriter = new StringWriter();
			throwable.printStackTrace(new PrintWriter(stackTraceWriter));
			return stackTraceWriter.toString();
		} else {
			return throwable.getMessage();
		}
	};
}
