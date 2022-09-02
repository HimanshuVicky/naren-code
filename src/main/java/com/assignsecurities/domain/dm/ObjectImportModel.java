package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.sql.Date;


public class ObjectImportModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3333241756198674972L;
	// ID ,OBJ_ID,LOCALE_CODE, IMPORTED_BY,FILE_NAME,FILE_BYTE, RETRY_FILE_NAME,
	// RETRY_FILE,STATUS_ID, ERROR_REC_COUNT, TOTAL_REC_COUNT, FILE_FORMAT
	// ,DATE_CREATED, DATE_MODIFIED ,MODIFIED_BY


	private Long id;

	private Long objId;

	private String localeCode;


	private Long importedBy;

	private String fileName;

	private byte[] fileByte;

	private String retryFileName;

	private byte[] retryByte;

	private Long statusId;

	private Long errorRecordCount;

	private Long totalRecordCount;

	private String fileFormat;

	private Date dateCreated;

	private Date dateModified;

	private Long modifiedBy;
	
	private String fileContent;
	private String fileExt;
	
//	@Transient
	private String statusString;

//	@Transient
	private String dateModifiedFormated;
	
//	@Transient
	private Long totalSucessRecords;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the objId
	 */
	public Long getObjId() {
		return objId;
	}

	/**
	 * @param objId the objId to set
	 */
	public void setObjId(Long objId) {
		this.objId = objId;
	}

	/**
	 * @return the localeCode
	 */
	public String getLocaleCode() {
		return localeCode;
	}

	/**
	 * @param localeCode the localeCode to set
	 */
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	

//	public PdmsSecurityUser getImportedBy() {
//		return importedBy;
//	}
//
//	public void setImportedBy(PdmsSecurityUser importedBy) {
//		this.importedBy = importedBy;
//	}
//
//	public void setModifiedBy(PdmsSecurityUser modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}

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
	 * @return the fileByte
	 */
	public byte[] getFileByte() {
		return fileByte;
	}

	/**
	 * @param fileByte the fileByte to set
	 */
	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}

	/**
	 * @return the retryFileName
	 */
	public String getRetryFileName() {
		return retryFileName;
	}

	/**
	 * @param retryFileName the retryFileName to set
	 */
	public void setRetryFileName(String retryFileName) {
		this.retryFileName = retryFileName;
	}

	/**
	 * @return the retryByte
	 */
	public byte[] getRetryByte() {
		return retryByte;
	}

	/**
	 * @param retryByte the retryByte to set
	 */
	public void setRetryByte(byte[] retryByte) {
		this.retryByte = retryByte;
	}

	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the errorRecordCount
	 */
	public Long getErrorRecordCount() {
		return errorRecordCount;
	}

	/**
	 * @param errorRecordCount the errorRecordCount to set
	 */
	public void setErrorRecordCount(Long errorRecordCount) {
		this.errorRecordCount = errorRecordCount;
	}

	/**
	 * @return the totalRecordCount
	 */
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * @param totalRecordCount the totalRecordCount to set
	 */
	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	/**
	 * @return the fileFormat
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param fileFormat the fileFormat to set
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
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

	/**
	 * @return the dateModified
	 */
	public Date getDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

//	public PdmsSecurityUser getModifiedBy() {
//		return modifiedBy;
//	}

	public String getDateModifiedFormated() {
		return dateModifiedFormated;
	}

	public void setDateModifiedFormated(String dateModifiedFormated) {
		this.dateModifiedFormated = dateModifiedFormated;
	}

	/**
	 * @return the totalSucessRecords
	 */
	public Long getTotalSucessRecords() {
		if (this.totalSucessRecords == null && this.totalRecordCount != null
				&& this.errorRecordCount != null) {
			return (this.totalRecordCount - this.errorRecordCount);
		}
		return totalSucessRecords;
	}

	/**
	 * @param totalSucessRecords the totalSucessRecords to set
	 */
	public void setTotalSucessRecords(Long totalSucessRecords) {
		this.totalSucessRecords = totalSucessRecords;
	}

	/**
	 * @return the importedBy
	 */
	public Long getImportedBy() {
		return importedBy;
	}

	/**
	 * @param importedBy the importedBy to set
	 */
	public void setImportedBy(Long importedBy) {
		this.importedBy = importedBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public Long getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the fileContent
	 */
	public String getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	/**
	 * @return the fileExt
	 */
	public String getFileExt() {
		return fileExt;
	}

	/**
	 * @param fileExt the fileExt to set
	 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

}
