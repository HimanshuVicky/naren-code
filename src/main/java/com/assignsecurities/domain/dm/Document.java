package com.assignsecurities.domain.dm;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 
 *
 */
public class Document {
	private Long id;	
	private Long objectId;
	private String fileName;
	private String fileType;
	private long fileId;
	private FileInputStream fileImputStream;
	private InputStream imputStream;
	private Long importedBy;
	private int parentSheetId;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public FileInputStream getFileImputStream() {
		return fileImputStream;
	}
	public void setFileImputStream(FileInputStream fileImputStream) {
		this.fileImputStream = fileImputStream;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	/**
	 * @return the imputStream
	 */
	public InputStream getImputStream() {
		return imputStream;
	}
	/**
	 * @param imputStream the imputStream to set
	 */
	public void setImputStream(InputStream imputStream) {
		this.imputStream = imputStream;
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
	 * @return the parentSheetId
	 */
	public int getParentSheetId() {
		return parentSheetId;
	}
	/**
	 * @param parentSheetId the parentSheetId to set
	 */
	public void setParentSheetId(int parentSheetId) {
		this.parentSheetId = parentSheetId;
	}
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	
}
