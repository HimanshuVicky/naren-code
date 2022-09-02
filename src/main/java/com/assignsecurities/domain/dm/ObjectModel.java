package com.assignsecurities.domain.dm;

import java.io.Serializable;


public class ObjectModel  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2250077148166871269L;

	private Long id;
	
	private String name;
	
	private String code;
	
	private Long objectPerFile;
	
	private Long maxParentRows;

	private ObjectTemplateModel objectTemplateModel;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the objectPerFile
	 */
	public Long getObjectPerFile() {
		return objectPerFile;
	}


	/**
	 * @param objectPerFile the objectPerFile to set
	 */
	public void setObjectPerFile(Long objectPerFile) {
		this.objectPerFile = objectPerFile;
	}


	/**
	 * @return the maxParentRows
	 */
	public Long getMaxParentRows() {
		return maxParentRows;
	}


	/**
	 * @param maxParentRows the maxParentRows to set
	 */
	public void setMaxParentRows(Long maxParentRows) {
		this.maxParentRows = maxParentRows;
	}


	/**
	 * @return the objectTemplateModel
	 */
	public ObjectTemplateModel getObjectTemplateModel() {
		return objectTemplateModel;
	}


	/**
	 * @param objectTemplateModel the objectTemplateModel to set
	 */
	public void setObjectTemplateModel(ObjectTemplateModel objectTemplateModel) {
		this.objectTemplateModel = objectTemplateModel;
	}
	
	
}
