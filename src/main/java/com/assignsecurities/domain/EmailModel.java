package com.assignsecurities.domain;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;




public class EmailModel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2526125097311365661L;
	private Long id;
	private String fromEmail;
	private String subject;
	private Long statusId;
	private Date dateCreated;
	private Date dateModified;
	private String createdBy;
	private String modifiedBy;

	private String comments;

	private List<EmailToModel> emailTos;

	private List<EmailCcModel> emailCcs;

	private List<EmailBccModel> emailBccs;

	private EmailBodyModel emailBody;
	
//	private FileAttachmentModel [] attachmentFiles;
	private List<FileAttachmentModel> fileAttachmentModels;
	
	private List<EmailAttachmentModel> attachmentModels;

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
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * @param fromEmail
	 *            the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the statusId
	 * 
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId
	 *            the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	

	/**
	 * @return the emailTos
	 */
	public List<EmailToModel> getEmailTos() {
		return emailTos;
	}

	/**
	 * @param emailTos
	 *            the emailTos to set
	 */
	public void setEmailTos(List<EmailToModel> emailTos) {
		this.emailTos = emailTos;
	}

	/**
	 * @return the emailCcs
	 */
	public List<EmailCcModel> getEmailCcs() {
		return emailCcs;
	}

	/**
	 * @param emailCcs
	 *            the emailCcs to set
	 */
	public void setEmailCcs(List<EmailCcModel> emailCcs) {
		this.emailCcs = emailCcs;
	}

	/**
	 * @return the emailBccs
	 */
	public List<EmailBccModel> getEmailBccs() {
		return emailBccs;
	}

	/**
	 * @param emailBccs
	 *            the emailBccs to set
	 */
	public void setEmailBccs(List<EmailBccModel> emailBccs) {
		this.emailBccs = emailBccs;
	}

	/**
	 * @return the emailBody
	 */
	public EmailBodyModel getEmailBody() {
		return emailBody;
	}

	/**
	 * @param emailBody
	 *            the emailBody to set
	 */
	public void setEmailBody(EmailBodyModel emailBody) {
		this.emailBody = emailBody;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	
	
	public List<FileAttachmentModel> getFileAttachmentModels() {
		return fileAttachmentModels;
	}

	public void setFileAttachmentModels(List<FileAttachmentModel> fileAttachmentModels) {
		this.fileAttachmentModels = fileAttachmentModels;
	}

	public List<EmailAttachmentModel> getAttachmentModels() {
		return attachmentModels;
	}

	public void setAttachmentModels(List<EmailAttachmentModel> attachmentModels) {
		this.attachmentModels = attachmentModels;
	}

	@Override
	public String toString() {
		return "EmailModel [id=" + id + ", fromEmail=" + fromEmail + ", subject=" + subject + ", statusId=" + statusId
				+ ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", comments=" + comments + ", emailTos=" + emailTos + ", emailCcs="
				+ emailCcs + ", emailBccs=" + emailBccs + ", emailBody=" + emailBody + "]";
	}

	
}
