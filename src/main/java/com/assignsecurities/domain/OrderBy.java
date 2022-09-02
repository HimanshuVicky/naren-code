package com.assignsecurities.domain;

public class OrderBy {

	private String property;
	
	private String propertyOrder=Pagination.SORT_ORDER_ASC;

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the propertyOrder
	 */
	public String getPropertyOrder() {
		return propertyOrder;
	}

	/**
	 * @param propertyOrder the propertyOrder to set
	 */
	public void setPropertyOrder(String propertyOrder) {
		this.propertyOrder = propertyOrder;
	}
	
	
}
