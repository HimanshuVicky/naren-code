package com.assignsecurities.domain;

public class EmailToModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1805111659027781152L;
	private Long id;
	private String email;

	

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
	 * @return the eMail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param eMail
	 *            the eMail to set
	 */
	/**
	 * @return the pdmsOrgStudentDetailPickup
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	

}
