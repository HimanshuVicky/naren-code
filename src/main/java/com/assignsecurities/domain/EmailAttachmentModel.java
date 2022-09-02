package com.assignsecurities.domain;

public class EmailAttachmentModel  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4883384769813915512L;
	private Long id;
	private Long emailId;
	private Long attachmentDocId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmailId() {
		return emailId;
	}
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	public Long getAttachmentDocId() {
		return attachmentDocId;
	}
	public void setAttachmentDocId(Long attachmentDocId) {
		this.attachmentDocId = attachmentDocId;
	}
	
	
}
