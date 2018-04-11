package com.demo.fn.config.props;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Loads the properties for Couchbase from {@code application.yml} file.
 *
 * @author Suraj Mohanan Kodiyath
 */
@ConfigurationProperties("app.couchbase")
public class CouchbaseConfigProperties {

	private List<String> hosts;
	private boolean sslEnabled;
	private Long defaultTimeoutInSecs;
	private Long connectionTimeoutInSecs;
	private Long keyValueTimeoutInSecs;
	private Long queryTimeoutInSecs;
	private Integer maxRetryAttempt;

	/**
	 * Returns the value of hosts.
	 *
	 * @return the hosts
	 */
	public List<String> getHosts() {
		return hosts;
	}

	/**
	 * Sets the value of hosts.
	 *
	 * @param hosts
	 *            the hosts to set
	 */
	public void setHosts(final List<String> hosts) {
		this.hosts = hosts;
	}

	/**
	 * Returns the value of sslEnabled.
	 *
	 * @return the sslEnabled
	 */
	public boolean isSslEnabled() {
		return sslEnabled;
	}

	/**
	 * Sets the value of sslEnabled.
	 *
	 * @param sslEnabled
	 *            the sslEnabled to set
	 */
	public void setSslEnabled(final boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	/**
	 * Returns the value of defaultTimeoutInSecs.
	 *
	 * @return the defaultTimeoutInSecs
	 */
	public Long getDefaultTimeoutInSecs() {
		return defaultTimeoutInSecs;
	}

	/**
	 * Sets the value of defaultTimeoutInSecs.
	 *
	 * @param defaultTimeoutInSecs
	 *            the defaultTimeoutInSecs to set
	 */
	public void setDefaultTimeoutInSecs(final Long defaultTimeoutInSecs) {
		this.defaultTimeoutInSecs = defaultTimeoutInSecs;
	}

	/**
	 * Returns the value of connectionTimeoutInSecs.
	 *
	 * @return the connectionTimeoutInSecs
	 */
	public Long getConnectionTimeoutInSecs() {
		return connectionTimeoutInSecs;
	}

	/**
	 * Sets the value of connectionTimeoutInSecs.
	 *
	 * @param connectionTimeoutInSecs
	 *            the connectionTimeoutInSecs to set
	 */
	public void setConnectionTimeoutInSecs(final Long connectionTimeoutInSecs) {
		this.connectionTimeoutInSecs = connectionTimeoutInSecs;
	}

	/**
	 * Returns the value of keyValueTimeoutInSecs.
	 *
	 * @return the keyValueTimeoutInSecs
	 */
	public Long getKeyValueTimeoutInSecs() {
		return keyValueTimeoutInSecs;
	}

	/**
	 * Sets the value of keyValueTimeoutInSecs.
	 *
	 * @param keyValueTimeoutInSecs
	 *            the keyValueTimeoutInSecs to set
	 */
	public void setKeyValueTimeoutInSecs(final Long keyValueTimeoutInSecs) {
		this.keyValueTimeoutInSecs = keyValueTimeoutInSecs;
	}

	/**
	 * Returns the value of queryTimeoutInSecs.
	 *
	 * @return the queryTimeoutInSecs
	 */
	public Long getQueryTimeoutInSecs() {
		return queryTimeoutInSecs;
	}

	/**
	 * Sets the value of queryTimeoutInSecs.
	 *
	 * @param queryTimeoutInSecs
	 *            the queryTimeoutInSecs to set
	 */
	public void setQueryTimeoutInSecs(final Long queryTimeoutInSecs) {
		this.queryTimeoutInSecs = queryTimeoutInSecs;
	}

	/**
	 * Returns the value of maxRetryAttempt.
	 *
	 * @return the maxRetryAttempt
	 */
	public Integer getMaxRetryAttempt() {
		return maxRetryAttempt;
	}

	/**
	 * Sets the value of maxRetryAttempt.
	 *
	 * @param maxRetryAttempt
	 *            the maxRetryAttempt to set
	 */
	public void setMaxRetryAttempt(final Integer maxRetryAttempt) {
		this.maxRetryAttempt = maxRetryAttempt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CouchbaseConfigs {hosts=");
		builder.append(hosts);
		builder.append(", sslEnabled=");
		builder.append(sslEnabled);
		builder.append(", defaultTimeoutInSecs=");
		builder.append(defaultTimeoutInSecs);
		builder.append(", connectionTimeoutInSecs=");
		builder.append(connectionTimeoutInSecs);
		builder.append(", keyValueTimeoutInSecs=");
		builder.append(keyValueTimeoutInSecs);
		builder.append(", queryTimeoutInSecs=");
		builder.append(queryTimeoutInSecs);
		builder.append(", maxRetryAttempt=");
		builder.append(maxRetryAttempt);
		builder.append("}");
		return builder.toString();
	}

}
