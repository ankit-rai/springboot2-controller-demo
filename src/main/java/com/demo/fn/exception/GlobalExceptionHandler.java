package com.demo.fn.exception;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.util.WebConstants;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	public GlobalExceptionHandler(final ApplicationContext applicationContext, final ServerCodecConfigurer serverCodecConfigurer) {
		super (new DemoErrorAttributes(), new ResourceProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}
	
	/**
	 * Render the error information as a JSON payload.
	 * @param request the current request
	 * @return a {@code Publisher} of the HTTP response
	 */
	private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
		final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
		
		final HttpStatus httpStatus = (HttpStatus) errorPropertiesMap.get(WebConstants.HTTP_STATUS_KEY);
		
		// Remove the HttpStatus from the map so that it does not get rendered in the response 
		errorPropertiesMap.remove(WebConstants.HTTP_STATUS_KEY);
		
		// Store request context in the map
        logger.info("RequestContext from Server Request --> {}", request.attribute(RequestTxContext.CLASS_NAME));
        
        final RequestTxContext requestTxContext = (RequestTxContext) request.attribute(RequestTxContext.CLASS_NAME).orElse(null);
		
		return ServerResponse.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-TraceId", requestTxContext == null ? "No-TxId" : requestTxContext.getTxId())
				.body(BodyInserters.fromObject(errorPropertiesMap));
	}
}
