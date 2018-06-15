package com.demo.fn.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.instrument.web.TraceWebFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import brave.Span;
import brave.Tracing;
import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class CustomTracingHeaderFilter implements WebFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomTracingHeaderFilter.class);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("CustomTracingHeaderFilter was called.");
        
        return Mono.subscriberContext()
            .flatMap(context -> {
                logger.info("CustomTracingHeaderFilter:: Inside Mono.subscriberContext.flatMap");
                
                // Check if client trace id is available
                final String clientTraceIdHeader = exchange.getRequest().getHeaders().getFirst("X-Client-Trace-Id");
                if (StringUtils.isNotBlank(clientTraceIdHeader)) {
                    // Add to response
                    exchange.getResponse().getHeaders().add("X-Client-Trace-Id", clientTraceIdHeader);
                }
                
                logger.info("Tracing.currentTracer().currentSpan(): {}", Tracing.currentTracer().currentSpan());
                
                Span span = context.get(Span.class);
                if (span == null) {
                    logger.info("No span available in Subscriber Context; getting it from exchange.getAttribute");
                    span = (Span) exchange.getAttributes().get(TraceWebFilter.class.getName() + ".TRACE");
                } else {
                    logger.info("Got span from Reactor Context.");
                }
                
                logger.info("CustomTracingHeaderFilter:: span.context().traceIdString(): {}", span.context().traceIdString());
                exchange.getResponse().getHeaders().add("X-Server-Trace-Id", span.context().traceIdString());
                
                return chain.filter(exchange);
            })
        ;
    }
}
