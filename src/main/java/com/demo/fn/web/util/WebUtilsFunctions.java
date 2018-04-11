package com.demo.fn.web.util;

import com.demo.exception.ExceptionSpitter;
import com.demo.fn.exception.ExceptionCodes;
import com.demo.fn.exception.ExceptionMessages;
import com.demo.fn.util.WebConstants;
import com.demo.fn.web.model.ResourceDetail;
import com.demo.util.AppSupportFunctions;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.ServerRequest;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class WebUtilsFunctions {
    
    private static final Logger logger = LoggerFactory.getLogger(WebUtilsFunctions.class);
	public static final String CLASS_NAME = WebUtilsFunctions.class.getCanonicalName();
	
	private WebUtilsFunctions() {}
	
	public static final Function<String, Mono<ResourceDetail>> FN_GET_RESOURCE_DETAIL_FROM_PATH = requestPath -> {
		final String[] pathElementsArray = StringUtils.split(requestPath, "/");
		final int pathElementCount = pathElementsArray.length;
		
		if (pathElementCount <= 0) {
			throw ExceptionSpitter
				.forErrorCode(ExceptionCodes.REST_400003)
				.withErrorMessage(AppSupportFunctions.FN_FORMAT_STRING.apply(ExceptionMessages.REST_400003, new String[] {requestPath}))
				.spit();
		}
		
		final ResourceDetail resourceDetail = new ResourceDetail();
		if (pathElementCount >= 2) {
			resourceDetail.setResourceId(pathElementsArray[pathElementCount-1]);
			resourceDetail.setResourceType(pathElementsArray[pathElementCount-2]);
		} else {
			resourceDetail.setResourceType(pathElementsArray[0]);
		}
		
		return Mono.just(resourceDetail);
	};
	
	/**
	 * Gets HTTP status code from error code. E.g. if the code is 
	 * <strong> {@code ABC_400001}</strong>, then the extracted code will 
	 * be <strong>{@code 400}</strong>. 
	 */
	public static final Function<String, Integer> FN_GET_HTTP_STATUS_CODE_FROM_ERROR_CODE = errorCode -> {
		if (StringUtils.isNotBlank(errorCode)) {
			final Matcher matcher = WebConstants.HTTP_STATUS_CODE_WITHIN_ERROR_CODES_PATTERN.matcher(errorCode);
			if (matcher.find()) {
				try {
					return Integer.valueOf(StringUtils.substring(matcher.group(0), 1));
				} catch (final Exception e) {
				    logger.warn("Cannot determine HTTP status code from given error code '{}'", errorCode);
				}
			}
		}
		
		return Integer.valueOf(-1);
	};
	
	public static final BiFunction<ServerRequest, String, Optional<List<String>>> GET_HEADER_VALUES = (httpRequest, headerName) ->
        Optional.ofNullable(httpRequest.headers().header(headerName));
	
	public static final BiFunction<ServerRequest, String, Optional<String>> GET_FIRST_HEADER_VALUE = (httpRequest, headerName) ->
        GET_HEADER_VALUES.apply(httpRequest, headerName)
            .filter(CollectionUtils::isNotEmpty)
            .flatMap(list -> Optional.ofNullable(list.get(0)))
            ;
}
