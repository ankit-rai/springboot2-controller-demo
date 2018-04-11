package com.demo.fn.config.spring;

import com.demo.fn.config.props.CouchbaseConfigProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableConfigurationProperties(value = { 
		CouchbaseConfigProperties.class })
public class AppConfiguration {
}
