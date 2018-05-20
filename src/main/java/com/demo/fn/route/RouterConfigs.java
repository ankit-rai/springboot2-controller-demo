package com.demo.fn.route;

import com.demo.fn.web.filter.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.*;


/**
 * Configures the web routes.
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableWebFlux
public class RouterConfigs {
    
    private static final Logger logger = LoggerFactory.getLogger(RouterConfigs.class);
	
	@Autowired
	private UserApiHandler userApiHandler;
	
	@Autowired
	private StreamHandler streamHandler;
	
	@Autowired
	private HttpHeaderFilter httpHeaderFilter;
	
	@Autowired
	private AuthenticationFilter authenticationFilter;
	
	@Autowired
	private AuthorizationFilter authorizationFilter;
	
	@Autowired
	private ResourceParsingFilter resourceParsingFilter;
	
	@Autowired
	private RequestTxContextFilter requestTxContextFilter;
	
	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions
			.route(healthCheckPredicate(), request -> ServerResponse.ok().build())
			.andNest(streamPathPredicate(), nestedStreamRoutes())
			.andNest(emptyPathPredicate(), nestedBaseRoutes())
			;
	}

	private RequestPredicate healthCheckPredicate() {
		return RequestPredicates.GET("/health/check");
	}
	
	private RequestPredicate emptyPathPredicate() {
		return RequestPredicates.path("");
	}
	
	private RequestPredicate streamPathPredicate() {
		return RequestPredicates.path("/stream");
	}
	
	private RouterFunction<ServerResponse> nestedBaseRoutes() {
		return RouterFunctions
				.route(getByIdPredicate(), userApiHandler::getById)
				.andRoute(addResourcePredicate(), userApiHandler::addUser)
				.filter(requestTxContextFilter
						.andThen(httpHeaderFilter)
						.andThen(resourceParsingFilter)
						.andThen(authenticationFilter)
						.andThen(authorizationFilter))
				;
	}
	
	private RequestPredicate getByIdPredicate() {
	    logger.info("getByIdPredicate called....");
		return RequestPredicates.GET("/*/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				;
	}
	
	private RequestPredicate addResourcePredicate() {
		return RequestPredicates.POST("/*/")
				.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8))
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8));
	}
	
	private RouterFunction<ServerResponse> nestedStreamRoutes() {
		return RouterFunctions
				.route(streamGetByIdPredicate(), streamHandler::stream)
				.filter(requestTxContextFilter
						.andThen(httpHeaderFilter)
						.andThen(resourceParsingFilter)
						.andThen(authenticationFilter)
						.andThen(authorizationFilter))
				;
	}
	
	private RequestPredicate streamGetByIdPredicate() {
		return RequestPredicates
				.GET("/*/{id}")
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				;
	}
}
