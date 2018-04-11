package com.demo.util.logger;

/**
 * Defines the constants used for logging.
 * 
 * @author Niranjan Nanda
 */
public final class LoggerConstants {
	private LoggerConstants() {}
	
	public static final String SPLUNK_LOGGER = "SPLUNK_LOGGER";
	
	public static final String TX_ID_KEY = "TxId";
	public static final String TX_PATH_KEY = "TxPath"; // Both class and method names will be here.
	public static final String ERROR_MESSAGE_KEY = "ErrorMessage";
	public static final String TX_EXECUTION_TIME_IN_MS_KEY = "TxExecutionTimeInMs";
	public static final String TX_STATUS_KEY = "TxStatus";
	public static final String LOGGING_AGENT_CLASS_KEY = "LoggingAgent";
	
	public static final String LEFT_SQUARE_BRACKET = "[";
	public static final String RIGHT_SQUARE_BRACKET = "]";
	public static final String EQUALS = "=";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String SPACE = " ";
	public static final String DOUBLE_QUOTE_ESCAPE_STRING = "\\\"";
	
	public static final String TX_PATH_DELIMITER = "#";
}
