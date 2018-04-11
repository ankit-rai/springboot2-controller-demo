package com.demo.fn.web.filter;

import com.demo.exception.ExceptionSpitter;
import com.demo.fn.context.RequestTxContext;
import com.demo.fn.exception.ExceptionCodes;
import com.demo.fn.exception.ExceptionMessages;
import com.demo.fn.util.WebConstants;
import com.demo.util.AppSupportFunctions;
import com.demo.util.StreamUtil;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * Checks mandatory header
 * 
 * @author Niranjan Nanda
 */
public class HttpHeaderFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderFilter.class);
    
	/*
	 * Returning Optional from here allows me to chain further.
	 */
	private static final Function<String, Optional<Mono<ServerResponse>>> EXCEPTION_MAPPER = errorMessageArg -> 
		Optional.of(ExceptionSpitter
				.forErrorCode(ExceptionCodes.REST_400001)
				.withErrorMessage(AppSupportFunctions.FN_FORMAT_STRING.apply(ExceptionMessages.REST_400001, new String[] {errorMessageArg}))
				.spitAsMono());
	
	private final Set<String> requiredHttpHeaders;
	
	public HttpHeaderFilter(final String requiredHttpHeaderNames) {
	    
		if (StringUtils.isBlank(requiredHttpHeaderNames)) {
			requiredHttpHeaders = WebConstants.DEFAULT_REQUIRED_HEADERS_SET;
		} else {
			final String[] requiredHttpHeadersArray = StringUtils.split(requiredHttpHeaderNames, WebConstants.REQUIRED_HEADER_NAMES_DELIMITER);
			if (ArrayUtils.isEmpty(requiredHttpHeadersArray)) {
				requiredHttpHeaders = WebConstants.DEFAULT_REQUIRED_HEADERS_SET;
			} else {
				requiredHttpHeaders = StreamUtil.asStream(requiredHttpHeadersArray, false).collect(Collectors.toSet());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
		return Mono.subscriberContext()
    		.flatMap(context -> {
    		    final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
    		    logger.info("[HttpHeaderFilter was called] Request Tx Context --> {}", requestTxContext);
    
    		    return requiredHttpHeaders.stream()
                    .filter(header -> CollectionUtils.isEmpty(request.headers().header(header))) // Checks if a mandatory header is missing 
                    .findFirst()
                    .flatMap(missingHeader -> {
                        logger.info("[HttpHeaderFilter#filter][TxId: {}] Missing header --> {}", requestTxContext.getTxId(), missingHeader);
                        return EXCEPTION_MAPPER.apply(missingHeader);
                     })
                    .orElse(next.handle(request));
    		})
    		;
		
		
//		return requiredHttpHeaders.stream()
//			.filter(header -> CollectionUtils.isEmpty(request.headers().header(header))) // Checks if a mandatory header is missing 
//			.findFirst() // Get the first header that passed the previous filter predicate
//			.flatMap(EXCEPTION_MAPPER) // If such a header was found, convert that to Mono<ServerResponse> with error in it. (this is a way to throw validation exception
//			.orElse(next.handle(request)) // If all mandatory headers are present, delegate to next filter.
//			;
	}
	
}
