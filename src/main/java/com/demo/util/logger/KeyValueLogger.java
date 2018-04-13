package com.demo.util.logger;

import com.demo.fn.context.RequestTxContext;
import com.demo.util.StreamUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import reactor.core.publisher.Signal;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * This provides a fluent style API for key-value logging.
 * 
 * 
 * @author Niranjan Nanda
 */
public final class KeyValueLogger {
	private final Map<String, Object> kvData;
	private final Logger targetClazzLogger;
	
    private static final Logger KV_LOGGER = LoggerFactory.getLogger("KV_LOGGER");
	
	public KeyValueLogger(final Logger targetClazzLogger) {
		this.kvData = new LinkedHashMap<>();
		this.targetClazzLogger = targetClazzLogger;
	}
	
	public KeyValueLogger addTxId(final String txId) {
		this.kvData.put(LoggerConstants.TX_ID_KEY, txId);
		return this;
	}
	
	public KeyValueLogger addTxStatus(final TxStatus txStatus) {
		this.kvData.put(LoggerConstants.TX_STATUS_KEY, txStatus);
		return this;
	}
	
	public KeyValueLogger addTxPath(final String txPath) {
		this.kvData.put(LoggerConstants.TX_PATH_KEY, txPath);
		return this;
	}
	
	public KeyValueLogger addTxExecutionTime(final Long txStartTime) {
		this.kvData.put(LoggerConstants.TX_EXECUTION_TIME_IN_MS_KEY, 
				txStartTime == null ? Long.valueOf(-1L) : (System.currentTimeMillis() - txStartTime));
		return this;
	}
	
	public KeyValueLogger addException(final Throwable exception) {
		this.kvData.put(LoggerConstants.ERROR_MESSAGE_KEY, exception);
		return this;
	}
	
	public KeyValueLogger add(final String key, final Object value) {
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("Null key cannot be added");
		}
		
		this.kvData.put(key, value);
		return this;
	}
	
	public void log() {
	    this.targetClazzLogger.info("KeyValueLogger#log is called....");
		logInternal(LocationAwareLogger.INFO_INT); 
	}
	
	public void debug() {
		logInternal(LocationAwareLogger.DEBUG_INT);
	}
	
	public void info() {
		logInternal(LocationAwareLogger.INFO_INT);
	}
	
	public void warn() {
		logInternal(LocationAwareLogger.WARN_INT);
	}
	
	public void error() {
		logInternal(LocationAwareLogger.ERROR_INT);
	}
	
	public <T> Consumer<Signal<T>> consumeLog() {
	    return signal -> {
	        this.targetClassLogger().info("[KeyValueLogger#log] Caught Signal --> {}", signal);
	        if (!(signal.isOnNext() || signal.isOnError())) {
	            return;
	        }
	        
	        if (signal.hasError()) {
	            this.addException(signal.getThrowable());
	            this.addTxStatus(TxStatus.FAILURE);
	        } else {
	            this.addTxStatus(TxStatus.SUCCESS);
	        }

	        final Context context = signal.getContext();

	        this.targetClassLogger().info("[KeyValueLogger#log] Context inside kv logger consumer --> {}", context);

	        final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
	        this.addTxId(requestTxContext.getTxId());
	        this.logInternal(LocationAwareLogger.INFO_INT);
	    };
	}
	
	public Map<String, Object> dataMap() {
	    return Collections.unmodifiableMap(kvData);
	}
	
	public Logger targetClassLogger() {
	    return this.targetClazzLogger;
	}
	
	private void logInternal(final int level) {
	    final StringBuilder messageBuilder = new StringBuilder();
	    
	    if (!this.kvData.containsKey(LoggerConstants.TX_ID_KEY)) {
	        throw new IllegalArgumentException("TxID is required to log a message.");
	    }
	    
	    // Print the txId before anything else
	    messageBuilder
            .append(LoggerConstants.TX_ID_KEY)
            .append(LoggerConstants.EQUALS)
            .append(LoggerConstants.DOUBLE_QUOTE)
            .append(kvData.get(LoggerConstants.TX_ID_KEY))
            .append(LoggerConstants.DOUBLE_QUOTE)
            .append(LoggerConstants.SPACE);
	    
	    // Remove TxId from the map
	    kvData.remove(LoggerConstants.TX_ID_KEY);
	    
		StreamUtil.asStreamOfEntries(this.kvData, false)
			.filter(entry -> Objects.nonNull(entry) && StringUtils.isNotBlank(entry.getKey()))
			.map(this::toMessageTuple)
			.collect(
					() -> null, // Void supplier
					(voidSupplier, tuple2) -> 
							messageBuilder
								.append(tuple2.getT1())
								.append(LoggerConstants.EQUALS)
								.append(LoggerConstants.DOUBLE_QUOTE)
								.append(tuple2.getT2())
								.append(LoggerConstants.DOUBLE_QUOTE)
								.append(LoggerConstants.SPACE),
					(v1, v2) -> {} // A No-Op combiner combining two void suppliers.
				);
		
		switch(level) {
			case LocationAwareLogger.TRACE_INT:
				KV_LOGGER.trace("{}", messageBuilder);
				break;
			case LocationAwareLogger.DEBUG_INT:
				KV_LOGGER.debug("{}", messageBuilder);
				break;
			case LocationAwareLogger.INFO_INT:
				KV_LOGGER.info("{}", messageBuilder);
				break;
			case LocationAwareLogger.WARN_INT:
				KV_LOGGER.warn("{}", messageBuilder);
				break;
			case LocationAwareLogger.ERROR_INT:
				KV_LOGGER.error("{}", messageBuilder);
				break;
			default:
				KV_LOGGER.info("{}", messageBuilder);
				break;
		}

		// Clear the data map
		this.kvData.clear();
	}
	
	private Tuple2<String, String> toMessageTuple(final Map.Entry<String, Object> entry) {
		final String key = entry.getKey();
		final Object value = entry.getValue();
		
		if ( value != null && value instanceof Throwable) {
			// Decorator
			return Tuples.of(key, LoggerUtilFunctions.FN_THROWABLE_TO_MESSAGE_STRING
					.andThen(LoggerUtilFunctions.FN_KV_LOG_ENTRY_SANITIZER)
					.apply((Throwable) value, this.targetClazzLogger.isDebugEnabled()));
		}
		
		if (StringUtils.equals(LoggerConstants.TX_EXECUTION_TIME_IN_MS_KEY, key)) {
			if (value == null || ((Long) value).longValue() < 0L) {
				return Tuples.of(key, "undefined");
			} else {
				return Tuples.of(key, Objects.toString(value));
			}
		}
		
		return Tuples.of(key, LoggerUtilFunctions.FN_KV_LOG_ENTRY_SANITIZER.apply(Objects.toString(value)));
	}
}
