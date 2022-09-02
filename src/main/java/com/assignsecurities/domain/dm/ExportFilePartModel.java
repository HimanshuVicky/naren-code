package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.util.Date;

public class ExportFilePartModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 381106767539523503L;
	//ID,DL_EXP_FILES_ID ,FILE_NAME,FILE_ID,DATE_CREATED
	private long id;
	private long expFilesId;
	private String fileName;
	private long fileId;
	private Date dateCreated;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the expFilesId
	 */
	public long getExpFilesId() {
		return expFilesId;
	}
	/**
	 * @param expFilesId the expFilesId to set
	 */
	public void setExpFilesId(long expFilesId) {
		this.expFilesId = expFilesId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileId
	 */
	public long getFileId() {
		return fileId;
	}
	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}
