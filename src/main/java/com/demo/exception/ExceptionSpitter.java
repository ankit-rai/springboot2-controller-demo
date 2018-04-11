package com.demo.exception;

import reactor.core.publisher.Mono;

/**
 * A utility class to create an {@link PlatformException}
 * 
 * @author Niranjan Nanda
 */
public final class ExceptionSpitter {
	
	public static final String DEFAULT_ERROR_CODE = "_500000";
	public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong while processing your request. Please contact support team.";
	
	private ExceptionSpitter() {
		// Stops instantiation.
	}
	
	/**
	 * Creates an {@link PlatformException} with {@link #DEFAULT_ERROR_CODE}
	 * and {@link #DEFAULT_ERROR_MESSAGE}.
	 * 
	 * @return	A new instance of {@code AppSupportException} with default code
	 * and message.
	 */
	public static PlatformException spitDefault() {
		return new PlatformException(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE);
	}
	
	/**
	 * Creates an instance of internal builder class with the given 
	 * {@code errorCode}. The obtained builder class provides fluent-style
	 * chained methods to construct an {@link PlatformException}.
	 * 
	 * @param errorCode	The error code which will be used to create {@code AppSupportException}.
	 * @return	An instance of internal builder class.
	 */
	public static Spitter forErrorCode(final String errorCode) {
		return new Spitter(errorCode);
	}
	
	public static ExceptionEnumSpitter forExceptionEnum(final ExceptionEnum exceptionEnum) {
		return new ExceptionEnumSpitter(exceptionEnum);
	}
	
	/**
	 * The internal builder class which provides fluent style
	 * chained methods to construct {@link PlatformException}.
	 * 
	 * @author Niranjan Nanda
	 */
	public static final class Spitter {
		private final String errorCode;
		private String errorMessage;
		private Throwable cause;
		
		public Spitter(final String errorCode) {
			this.errorCode = errorCode;
		}
		
		/**
		 * Collects the error message which will be used while
		 * constructing {@link PlatformException}.
		 * 
		 * @param errorMessage	The error message that will be associated
		 * with {@link PlatformException}.
		 * @return	An existing instance of internal builder class.
		 */
		public Spitter withErrorMessage(final String errorMessage) {
			this.errorMessage= errorMessage;
			return this;
		}
		
		/**
		 * Collects the cause which will be wrapped in {@link PlatformException}. 
		 * 
		 * @param cause	The {@link Throwable} which will be wrapped in 
		 * {@link PlatformException}.
		 * @return	An existing instance of internal builder class.
		 */
		public Spitter withCause(final Throwable cause) {
			this.cause = cause;
			return this;
		}
		
		/**
		 * Creates a new instance of {@link PlatformException}.
		 * 
		 * @return	Newly created instance of {@code AppSupportException}.
		 */
		public PlatformException spit() {
			return new PlatformException(
					this.errorCode,
					this.errorMessage,
					cause);
		}
		
		/**
		 * Creates a {@link Mono} that will terminate with the {@link PlatformException}.
		 * This utility method is useful while working with {@code Mono} where an exception
		 * should not be thrown, rather should be given to the subscriber through error 
		 * channel.
		 * 
		 * @param <T> The type of the subscriber which is subscribing to {@link Mono}.
		 * @return	A {@link Mono} of the type that the subscriber is subscribing to.
		 */
		public <T> Mono<T> spitAsMono() {
			return Mono.error(spit());
		}
	}
	
	public static final class ExceptionEnumSpitter {
		private final ExceptionEnum exceptionEnum;
		private String[] messageArgs;
		private Throwable cause;
		
		public ExceptionEnumSpitter(final ExceptionEnum exceptionEnum) {
			this.exceptionEnum = exceptionEnum;
		}
		
		public ExceptionEnumSpitter forErrorMessageArgs(final String[] messageArgs) {
			this.messageArgs = messageArgs;
			return this;
		}
		
		/**
		 * Collects the cause which will be wrapped in {@link PlatformException}. 
		 * 
		 * @param cause	The {@link Throwable} which will be wrapped in 
		 * {@link PlatformException}.
		 * @return	An existing instance of internal builder class.
		 */
		public ExceptionEnumSpitter withCause(final Throwable cause) {
			this.cause = cause;
			return this;
		}
		
		public PlatformException spit() {
			return new Spitter(this.exceptionEnum.exceptionCode())
					.withErrorMessage(this.exceptionEnum.toFormattedExceptionMessage(this.messageArgs))
					.withCause(this.cause)
					.spit();
					
		}
		
		public <T> Mono<T> spitAsMono() {
			return Mono.error(spit());
					
		}
	}
}
