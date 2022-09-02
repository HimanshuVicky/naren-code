package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.util.Set;

public class ObjectConfigModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String objectName;

	private String tabOrFileName;

	private String businessObjectClassName;

	private boolean isRequired;

	private int headerRowNumber = 2;

	private String inputFileFormat;

	private int tabIndex;

	private String primaryColumnIndex = "1";

	private String parentReferanceColumnIndex = "1";

	private String objectType;

	private String businessValidatorClassName;

	private String businessProcesserClassName;

	private String dataDownLoadProcessorClassName;

	private int errorColumnIndex;

	private String objectAssemplerClassName;

	private String objectDisAssemplerClassName;

	private int dataStartRowNo;

	private boolean isActionColumnRequired;

	private Set<AttributeConfigModel> attributeConfigModels;

	public int getErrorColumnIndex() {
		return errorColumnIndex;
	}

	public void setErrorColumnIndex(int errorColumnIndex) {
		this.errorColumnIndex = errorColumnIndex;
	}

	/**
	 * @return the primaryColumnIndex
	 */
	public String getPrimaryColumnIndex() {
		return primaryColumnIndex;
	}

	/**
	 * @param primaryColumnIndex
	 *            the primaryColumnIndex to set
	 */
	public void setPrimaryColumnIndex(String primaryColumnIndex) {
		this.primaryColumnIndex = primaryColumnIndex;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getBusinessObjectClassName() {
		return businessObjectClassName;
	}

	public void setBusinessObjectClassName(String businessObjectClassName) {
		this.businessObjectClassName = businessObjectClassName;
	}

	public String getTabOrFileName() {
		return tabOrFileName;
	}

	public void setTabOrFileName(String tabOrFileName) {
		this.tabOrFileName = tabOrFileName;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getInputFileFormat() {
		return inputFileFormat;
	}

	public void setInputFileFormat(String inputFileFormat) {
		this.inputFileFormat = inputFileFormat;
	}

	public Set<AttributeConfigModel> getAttributeConfigModels() {
		return attributeConfigModels;
	}

	public void setAttributeConfigModels(
			Set<AttributeConfigModel> attributeConfigModels) {
		this.attributeConfigModels = attributeConfigModels;
	}

	public int getHeaderRowNumber() {
		return headerRowNumber;
	}

	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @return the parentReferanceColumnIndex
	 */
	public String getParentReferanceColumnIndex() {
		return parentReferanceColumnIndex;
	}

	/**
	 * @param parentReferanceColumnIndex
	 *            the parentReferanceColumnIndex to set
	 */
	public void setParentReferanceColumnIndex(String parentReferanceColumnIndex) {
		this.parentReferanceColumnIndex = parentReferanceColumnIndex;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType
	 *            the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getBusinessValidatorClassName() {
		return businessValidatorClassName;
	}

	public void setBusinessValidatorClassName(String businessValidatorClassName) {
		this.businessValidatorClassName = businessValidatorClassName;
	}

	public String getBusinessProcesserClassName() {
		return businessProcesserClassName;
	}

	public void setBusinessProcesserClassName(String businessProcesserClassName) {
		this.businessProcesserClassName = businessProcesserClassName;
	}

	public String getObjectAssemplerClassName() {
		return objectAssemplerClassName;
	}

	public void setObjectAssemplerClassName(String objectAssemplerClassName) {
		this.objectAssemplerClassName = objectAssemplerClassName;
	}

	/**
	 * @return the dataStartRowNo
	 */
	public int getDataStartRowNo() {
		return dataStartRowNo;
	}

	/**
	 * @param dataStartRowNo
	 *            the dataStartRowNo to set
	 */
	public void setDataStartRowNo(int dataStartRowNo) {
		this.dataStartRowNo = dataStartRowNo;
	}

	public String getObjectDisAssemplerClassName() {
		return objectDisAssemplerClassName;
	}

	public void setObjectDisAssemplerClassName(
			String objectDisAssemplerClassName) {
		this.objectDisAssemplerClassName = objectDisAssemplerClassName;
	}

	public String getDataDownLoadProcessorClassName() {
		return dataDownLoadProcessorClassName;
	}

	public void setDataDownLoadProcessorClassName(
			String dataDownLoadProcessorClassName) {
		this.dataDownLoadProcessorClassName = dataDownLoadProcessorClassName;
	}

	public boolean isActionColumnRequired() {
		return isActionColumnRequired;
	}

	public void setActionColumnRequired(boolean isActionColumnRequired) {
		this.isActionColumnRequired = isActionColumnRequired;
	}
}
