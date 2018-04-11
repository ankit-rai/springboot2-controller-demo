package com.demo.util;

import com.google.common.collect.MapMaker;

import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines utility {@link Function}s. 
 * 
 * @author Niranjan Nanda
 */
public final class AppSupportFunctions {
	private AppSupportFunctions() {}
	
	
    private static final Logger logger = LoggerFactory.getLogger(AppSupportFunctions.class);
	
	public static final String CLASS_NAME = AppSupportFunctions.class.getCanonicalName();
	
	/** Formats a given string with given arguments. */
	public static final BiFunction<String, Object[], String> FN_FORMAT_STRING = (message, args) -> {
		if (StringUtils.isBlank(message)) {
			return StringUtils.EMPTY;
		}
		
		try {
			return String.format(message, args);
		} catch (final MissingFormatArgumentException e) {
		    logger.info("Exception while formatting message '{}' with arguments '{}'", message, Arrays.toString(args));
			
			// Return the incoming message as-is.
			return message;
		}
	};
	
	public static final Supplier<ConcurrentMap<String, Object>> CONCURRENT_MAP_SUPPLIER = () -> new MapMaker()
			.concurrencyLevel(Runtime.getRuntime().availableProcessors()) // # of concurrent segments = available threads
			.initialCapacity(Runtime.getRuntime().availableProcessors() * 5) // Each segment will have 5 elements
			.makeMap();
}
