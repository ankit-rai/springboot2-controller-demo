package com.demo.fn.exception;

import com.demo.exception.ExceptionSpitter;
import com.demo.exception.PlatformException;
import com.demo.fn.util.WebConstants;
import com.demo.fn.web.util.WebUtilsFunctions;
import com.google.common.collect.Maps;

import java.util.Map;

import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * An implementation of {@link ErrorAttributes}. Provides the following attributes
 * when possible:
 * <ul>
 * <li>code - The application specific error code</li>
 * <li>error - The HTTP error reason phrase</li>
 * <li>debugMessage - A message which elaborates in detail why the request failed.</li>
 * </ul>
 * 
 * @author Niranjan Nanda
 */
public class DemoErrorAttributes implements ErrorAttributes {
	
	public static final String CLASS_NAME = DemoErrorAttributes.class.getCanonicalName();
	
	public static final String ERROR_ATTRIBUTE_KEY = "DemoErrorAttributes.ERROR";
	
	private final boolean includeException;
	
	/**
	 * 
	 */
	public DemoErrorAttributes() {
		this.includeException = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getErrorAttributes(final ServerRequest request, final boolean includeStackTrace) {
		final PlatformException platformException = (PlatformException) getError(request);
		
		final Map<String, Object> errorAttributesMap = Maps.newLinkedHashMap();
		errorAttributesMap.put(WebConstants.ERROR_CODE_KEY, platformException.getErrorCode());
		
		// Get HttpStatus from PlatformException error code.
		final int httpStatusCode = WebUtilsFunctions.FN_GET_HTTP_STATUS_CODE_FROM_ERROR_CODE.apply(platformException.getErrorCode());
		final HttpStatus httpStatus = HttpStatus.resolve(httpStatusCode);
		
		// Store the HttpStatus in the map
		errorAttributesMap.put(WebConstants.HTTP_STATUS_KEY, httpStatus);
		
		// Populate "error"
		errorAttributesMap.put(WebConstants.ERROR_REASON_PHRASE_KEY, httpStatus.getReasonPhrase());
		
		// Populate debug message from PlatformException
		errorAttributesMap.put(WebConstants.DEBUG_MESSAGE_KEY, platformException.getErrorMessage());
		
		return errorAttributesMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Throwable getError(final ServerRequest request) {
		return (Throwable) request.attribute(ERROR_ATTRIBUTE_KEY)
				.orElseThrow(ExceptionSpitter::spitDefault);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeErrorInformation(final Throwable error, final ServerWebExchange exchange) {
		PlatformException exceptionToStore = null;
		
		if (error == null) {
			exceptionToStore = ExceptionSpitter.spitDefault();
		} else if (error instanceof PlatformException) {
			exceptionToStore = (PlatformException) error;
		} else if (error.getCause() != null && error.getCause() instanceof PlatformException) {
			exceptionToStore = (PlatformException) error.getCause();
		} else {
			exceptionToStore = ExceptionSpitter.spitDefault();
		}

		exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE_KEY, exceptionToStore);
	}

	/**
	 * Returns the value of includeException.
	 *
	 * @return the includeException
	 */
	public boolean isIncludeException() {
		return includeException;
	}
}
