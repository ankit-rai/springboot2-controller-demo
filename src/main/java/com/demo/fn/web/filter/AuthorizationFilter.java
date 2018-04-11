package com.demo.fn.web.filter;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.web.model.ResourceDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * A web filter that validates the privilege of the given request.
 *
 * @author Suraj Mohanan Kodiyath
 */
public class AuthorizationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> next) {
		return Mono.subscriberContext()
		    .flatMap(context -> {
		        final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                logger.info("[AuthorizationFilter was called] Request Tx Context --> {}", requestTxContext);
                
                final ResourceDetail resourceDetail = (ResourceDetail) request.attribute("RESOURCE_DETAIL").get();
                logger.debug("[TxId: {}] Resource Detail -> {}", requestTxContext.getTxId(), resourceDetail);
                
                return Mono.just(resourceDetail);
		    })
		    .then(next.handle(request))
		;
		
		// Forward to next filter.
//		return next.handle(request);
	}

}
