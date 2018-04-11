package com.demo.fn.web.filter;

import com.demo.fn.context.RequestTxContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class RequestTxContextFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestTxContextFilter.class);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        final RequestTxContext requestTxContext = new RequestTxContext();
        
        logger.info("[RequestTxContextFilter was called] A request tx context was set: '{}'", requestTxContext);
        
        // Set it to ServerRequest so that GlobalExceptionHandler can use it.
        request.attributes().put(RequestTxContext.CLASS_NAME, requestTxContext);
        
        return next.handle(request)
                .subscriberContext(context -> Context.of(RequestTxContext.CLASS_NAME, requestTxContext));
    }
    
}
