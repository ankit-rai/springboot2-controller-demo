package com.demo.fn.web.filter;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.web.util.WebUtilsFunctions;

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
        // Look for a header X-Trace-Id. If available, use the ID sent in this header; else generate a new ID.
        final RequestTxContext requestTxContext =  WebUtilsFunctions.GET_FIRST_HEADER_VALUE.apply(request, "X-Trace-Id")
                .map(RequestTxContext::new)
                .orElse(new RequestTxContext());
        
        logger.info("[RequestTxContextFilter was called] A request tx context was set: '{}'", requestTxContext);
        
        // Set it to ServerRequest so that GlobalExceptionHandler can use it.
        request.attributes().put(RequestTxContext.CLASS_NAME, requestTxContext);
        
        return next.handle(request)
//                .map(serverResponse -> {
//                    return ServerResponse.from(serverResponse)
//                            .header("X-Trace-Id", requestTxContext.getTxId())
//                            .build();
//                })
//                .flatMap(Function.identity())
                .subscriberContext(context -> Context.of(RequestTxContext.CLASS_NAME, requestTxContext));
    }
    
}
