package com.demo.fn.util;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public final class WebConstants {
	private WebConstants() {}
	
	public static final String REQUIRED_HEADER_NAMES_DELIMITER = ",";
	
	public static final Set<String> DEFAULT_REQUIRED_HEADERS_SET = Sets.newHashSet(HttpHeaders.FROM, HttpHeaders.AUTHORIZATION);
	
	public static final String ERROR_CODE_KEY = "code";
	public static final String ERROR_REASON_PHRASE_KEY = "error";
	public static final String DEBUG_MESSAGE_KEY = "debugMessage";
	public static final String HTTP_STATUS_KEY = HttpStatus.class.getCanonicalName();
	
	/**
	 * <p>
	 * Pre-complied reg-ex pattern for HTTP Status code within error code strings. The error codes
	 * are assumed to follow a pattern <strong>{@code _XXX}</strong> like <strong>{@code SLP_401101}</strong>,
	 * <strong>{@code SLP_CLIENT_500001}</strong> etc.
	 * 
	 * <p>
	 * Example usage:
	 * <pre>
	 * final Matcher matcher = WebAppSupportConstants.HTTP_STATUS_CODE_WITHIN_ERROR_CODES_PATTERN.matcher(sourceString);
	 * if (matcher.find()) {
	 *     System.out.println(StringUtils.substring(matcher.group(0), 1));
	 * }
	 * </pre>
	 *
	 * The above code should print the HTTP status codes.
	 */
	public static final Pattern HTTP_STATUS_CODE_WITHIN_ERROR_CODES_PATTERN = Pattern.compile("_(1|2|3|4|5){1}\\d{2}");
}
