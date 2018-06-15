package com.demo.util.logger;

import org.apache.commons.lang3.StringUtils;

/**
 * An enum which provides status for transactions
 *
 * @author Niranjan Nanda
 */
public enum TxStatus {
	SUCCESS("Success"), FAILURE("Failure");

	/**
	 * Holds a string which is a sentence case representation of the Status.
	 */
	private String statusString;

	/**
	 * Constructor to create the enums.
	 *
	 * @param statusString The status string to which the enum must be initialized
	 */
	private TxStatus(final String statusString) {
		this.statusString = statusString;
	}

	/**
	 * Creates the enum from the given status string.
	 *
	 * @param statusString This is the value with which an enum will be created
	 * @return The enum whose internal <code><b>statusString</b></code> matches 
	 * the given <code><b>statusString</b></code>.
	 * This uses a case insensitive matching.
	 */
	public static TxStatus fromStatusString(final String statusString) {
		for (final TxStatus transactionStatus : values()) {
			if (StringUtils.equalsIgnoreCase(transactionStatus.getStatusString(), statusString)) {
				return transactionStatus;
			}
		}
		return null;
	}

	/**
	 * Returns the <code><b>statusString</b></code> with which the enum was initialized
	 *
	 * @return The <code><b>statusString</b></code> with which the enum was initialized
	 */
	@Override
	public String toString() {
		return this.statusString;
	}

	/**
	 * This behaves the same as <code><b>toString()</b></code>, but this method is provided for legibility.
	 *
	 * @return The <code><b>statusString</b></code> with which the enum was initialized
	 */
	public String getStatusString() {
		return this.statusString;
	}
}
