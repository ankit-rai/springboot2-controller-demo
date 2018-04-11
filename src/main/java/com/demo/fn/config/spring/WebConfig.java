package com.demo.fn.config.spring;

import com.demo.fn.web.filter.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Spring configuration for Web layer.
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableWebFlux
public class WebConfig  {
	
	@Value("${app.requiredHttpHeaders}")
	private String requiredHttpHeaders;
	
	@Bean
	public HttpHeaderFilter httpHeaderFilter() {
		return new HttpHeaderFilter(requiredHttpHeaders);
	}
	
	@Bean
	public AuthenticationFilter authenticationFilter() {
		return new AuthenticationFilter();
	}
	
	@Bean
	public AuthorizationFilter authorizationFilter() {
		return new AuthorizationFilter();
	}
	
	@Bean
	public ResourceParsingFilter resourceParsingFilter() {
		return new ResourceParsingFilter();
	}
	
	@Bean
	public RequestTxContextFilter requestContextFilter() {
	    return new RequestTxContextFilter();
	}
}
