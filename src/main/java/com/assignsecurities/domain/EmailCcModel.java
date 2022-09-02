package com.assignsecurities.domain;

public class EmailCcModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4342763043043008827L;
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
	 * @param email the eMail to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
}
