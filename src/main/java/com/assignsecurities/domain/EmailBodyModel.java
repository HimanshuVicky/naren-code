package com.assignsecurities.domain;

public class EmailBodyModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1048922803093478579L;
	private Long id;
	private byte[] eMailBoday;
	/**
	 * @return the id
	 */

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the eMailBoday
	 */
	public byte[] geteMailBoday() {
		return eMailBoday;
	}

	/**
	 * @param eMailBoday the eMailBoday to set
	 */
	public void seteMailBoday(byte[] eMailBoday) {
		this.eMailBoday = eMailBoday;
	}
}
