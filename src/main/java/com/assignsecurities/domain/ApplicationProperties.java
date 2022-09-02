package com.assignsecurities.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProperties implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4212049366775118261L;
	private Long id;
	private String propName;
	private String description;
	private String currentValue;
	private Boolean editable;
	private String origValue;
	private Date dateCreated;
	private Date dateModified;
	private String createdBy;
	private String modifiedBy;
	

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the propName
	 */
	public String getPropName() {
		return propName;
	}

	/**
	 * @param propName the propName to set
	 */
	public void setPropName(String propName) {
		this.propName = propName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the currentValue
	 */
	public String getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @return the editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the origValue
	 */
	public String getOrigValue() {
		return origValue;
	}

	/**
	 * @param origValue the origValue to set
	 */
	public void setOrigValue(String origValue) {
		this.origValue = origValue;
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

}
