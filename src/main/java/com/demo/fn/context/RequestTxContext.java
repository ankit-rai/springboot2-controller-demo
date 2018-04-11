package com.demo.fn.context;

import java.util.Objects;
import java.util.UUID;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class RequestTxContext {
    public static final String CLASS_NAME = RequestTxContext.class.getCanonicalName();
    
    private final String txId;
    private final long txStartTime;
    
    public RequestTxContext() {
        this(UUID.randomUUID().toString());
    }
    
    public RequestTxContext(final String txId) {
        this.txId = Objects.requireNonNull(txId, "Provided txId is null.");
        this.txStartTime = System.currentTimeMillis();
    }
    
    /**
     * Returns the value of txId.
     *
     * @return the txId
     */
    public String getTxId() {
        return txId;
    }
    /**
     * Returns the value of txStartTime.
     *
     * @return the txStartTime
     */
    public long getTxStartTime() {
        return txStartTime;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RequestContext {txId=");
        builder.append(txId);
        builder.append(", txStartTime=");
        builder.append(txStartTime);
        builder.append("}");
        return builder.toString();
    }
    
}
