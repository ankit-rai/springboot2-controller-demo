package com.demo.fn.web.filter;

import com.demo.exception.ExceptionSpitter;
import com.demo.exception.PlatformException;
import com.demo.fn.context.RequestTxContext;
import com.demo.fn.web.util.WebUtilsFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class ResourceParsingFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    
    private static final Logger logger = LoggerFactory.getLogger(ResourceParsingFilter.class);
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
	    
//		final String requestPath = request.path();
		
		return Mono.subscriberContext()
		    .flatMap(context -> {
		        final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                logger.info("[ResourceParsingFilter was called] Request Tx Context --> {}", requestTxContext);
                
                final String requestPath = request.path();
                
                return WebUtilsFunctions.FN_GET_RESOURCE_DETAIL_FROM_PATH.apply(requestPath)
                        .doOnError(t -> {
                            logger.error("[ResourceParsingFilter#filter][TxId: {}] Exception while extracting resource detail from request path '{}'...\n", requestTxContext.getTxId(), requestPath, t);
                            if (t instanceof PlatformException) {
                                throw (PlatformException) t;
                            } else {
                                throw ExceptionSpitter.spitDefault();
                            }
                        })
                        .doOnSuccess(resourceDetail -> request.attributes().put("RESOURCE_DETAIL", resourceDetail))
                        .then(next.handle(request))
                        ;
                
		    })
//		    .then(next.handle(request));
		    ;
		
//		return WebUtilsFunctions.FN_GET_RESOURCE_DETAIL_FROM_PATH.apply(requestPath)
//		    .doOnError(t -> logger.error("Exception while extracting resource detail from request path '{}'...\n", requestPath, t))
//		    .doOnSuccess(resourceDetail -> request.attributes().put("RESOURCE_DETAIL", resourceDetail))
//		    .then(next.handle(request));
	}
	
}
