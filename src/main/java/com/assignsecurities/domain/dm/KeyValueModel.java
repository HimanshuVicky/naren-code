package com.assignsecurities.domain.dm;

import java.io.Serializable;

public class KeyValueModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2763012932132261228L;

	private String idKey;
	
	private String idValue;

	/**
	 * @return the idKey
	 */
	public String getIdKey() {
		return idKey;
	}

	/**
	 * @param idKey the idKey to set
	 */
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}

	/**
	 * @return the idValue
	 */
	public String getIdValue() {
		return idValue;
	}

	/**
	 * @param idValue the idValue to set
	 */
	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

}
