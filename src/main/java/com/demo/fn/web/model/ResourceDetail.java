package com.demo.fn.web.model;

/**
 * Holds the resource details extracted from incoming
 * request.
 * 
 * @author Niranjan Nanda
 */
public class ResourceDetail {
	private String resourceId;
	private String resourceType;

	/**
	 * Returns the value of resourceId.
	 *
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * Sets the value of resourceId.
	 *
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * Returns the value of resourceType.
	 *
	 * @return the resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * Sets the value of resourceType.
	 *
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ResourceDetail {resourceId=");
		builder.append(resourceId);
		builder.append(", resourceType=");
		builder.append(resourceType);
		builder.append("}");
		return builder.toString();
	}
	
}
