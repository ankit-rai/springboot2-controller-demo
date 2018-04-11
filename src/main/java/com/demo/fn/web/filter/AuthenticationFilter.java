package com.demo.fn.web.filter;

import com.demo.exception.ExceptionSpitter;
import com.demo.fn.context.RequestTxContext;
import com.demo.fn.exception.ExceptionCodes;
import com.demo.fn.exception.ExceptionMessages;
import com.demo.fn.web.model.ResourceDetail;
import com.demo.fn.web.util.WebUtilsFunctions;
import com.demo.util.AppSupportFunctions;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * A filter which checks for <strong>{@code Authentication}</strong> HTTP header
 * and validates the content using A3 mechanism.
 *
 * @author Niranjan Nanda
 */
public class AuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    
	public static final String CLASS_NAME = AuthenticationFilter.class.getCanonicalName();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> next) {
		
		return Mono.subscriberContext()
		    .flatMap(context -> {
		        final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                logger.info("[AuthenticationFilter was called] Request Tx Context --> {}", requestTxContext);
                
		        final ResourceDetail resourceDetail = (ResourceDetail) request.attribute("RESOURCE_DETAIL").get();
		        logger.debug("[AuthenticationFilter#filter][TxId: {}] Resource Detail -> {}", requestTxContext.getTxId(), resourceDetail);
		        
		        final AtomicReference<String> tokenHolder = new AtomicReference<>();
		        return this.extractA3TokenFromHttpRequest(request)
		            .doOnNext(tokenHolder::set)
		            .flatMap(this::validateToken)
		            .switchIfEmpty(Mono.defer(() -> this.validateOTT(tokenHolder.get())))
		            .switchIfEmpty(Mono.defer(this::raiseException))
		            .doOnSuccess(isTokenValid -> logger.info("Authentication successful."))
		        ;
		    })
		    
		    // Forward to next filter
		    .then(next.handle(request));
	}
	
	private Mono<Boolean> validateToken(final String token) {
	    if (StringUtils.startsWith(token, "abc")) {
	        return Mono.just(Boolean.TRUE);
	    } else if (StringUtils.startsWith(token, "xyz")) {
            return Mono.just(Boolean.FALSE);
        }
	    
        return Mono.empty();
    }
    
    private Mono<Boolean> validateOTT(final String token) {
        if (StringUtils.startsWith(token, "ott")) {
            return Mono.just(Boolean.TRUE);
        } else if (StringUtils.startsWith(token, "xott")) {
            return Mono.just(Boolean.FALSE);
        }
        
        return Mono.empty();
    }
    
    private Mono<String> extractA3TokenFromHttpRequest(final ServerRequest request) {
        return Mono.justOrEmpty(WebUtilsFunctions.GET_FIRST_HEADER_VALUE
                .apply(request, HttpHeaders.AUTHORIZATION)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> ExceptionSpitter
                        .forErrorCode(ExceptionCodes.REST_400002)
                        .withErrorMessage(AppSupportFunctions.FN_FORMAT_STRING.apply(ExceptionMessages.REST_400002, new String[] {HttpHeaders.AUTHORIZATION}))
                        .spit()));

    }
    
    private Mono<Boolean> raiseException() {
        return ExceptionSpitter
                .forErrorCode(ExceptionCodes.REST_401001)
                .withErrorMessage(ExceptionMessages.REST_401001)
                .spitAsMono();
    }
}
