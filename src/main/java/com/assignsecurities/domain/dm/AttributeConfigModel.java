package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.util.List;


public class AttributeConfigModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2550527601263400548L;
	
	private long id;
	
	private String name;

	private String dataType;
	
	private boolean isRequired;
	
	private int length;
	
	private Long parentObjectId;
	
	private Long childObjectId;
	
	private boolean isAttributeAvailableInBo;
	
	private Long sequenceNo;
	
	private String dataFormatKey;
	
	private String headerName;
	
	private String delimitedKey;
	
	private String collectionDataType;
	
	private Integer decimalPlaces=-1;
	
	private String xmlTagName;
	
	private boolean isReverseValueUsed;
	
	private String sampleValue;
	
	private Long minValue;

	private Long maxValue;
	
	private boolean isXMLAttribute;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany()
//	@JoinColumn(name = "ATTRIBUTE_ID")
	private List<AttributeValueResolverModel> valueBasedResolvers;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany()
//	@JoinColumn(name = "ATTRIBUTE_ID")
	private List<AttributeValidationConfigModel> validationConFigModels;

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
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the parentObjectId
	 */
	public Long getParentObjectId() {
		return parentObjectId;
	}

	/**
	 * @param parentObjectId the parentObjectId to set
	 */
	public void setParentObjectId(Long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}

	/**
	 * @return the childObjectId
	 */
	public Long getChildObjectId() {
		return childObjectId;
	}

	/**
	 * @param childObjectId the childObjectId to set
	 */
	public void setChildObjectId(Long childObjectId) {
		this.childObjectId = childObjectId;
	}

	/**
	 * @return the isAttributeAvailableInBo
	 */
	public boolean isAttributeAvailableInBo() {
		return isAttributeAvailableInBo;
	}

	/**
	 * @param isAttributeAvailableInBo the isAttributeAvailableInBo to set
	 */
	public void setAttributeAvailableInBo(boolean isAttributeAvailableInBo) {
		this.isAttributeAvailableInBo = isAttributeAvailableInBo;
	}

	/**
	 * @return the sequenceNo
	 */
	public Long getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	/**
	 * @return the dataFormatKey
	 */
	public String getDataFormatKey() {
		return dataFormatKey;
	}

	/**
	 * @param dataFormatKey the dataFormatKey to set
	 */
	public void setDataFormatKey(String dataFormatKey) {
		this.dataFormatKey = dataFormatKey;
	}

	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return headerName;
	}

	/**
	 * @param headerName the headerName to set
	 */
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	/**
	 * @return the delimitedKey
	 */
	public String getDelimitedKey() {
		return delimitedKey;
	}

	/**
	 * @param delimitedKey the delimitedKey to set
	 */
	public void setDelimitedKey(String delimitedKey) {
		this.delimitedKey = delimitedKey;
	}

	/**
	 * @return the collectionDataType
	 */
	public String getCollectionDataType() {
		return collectionDataType;
	}

	/**
	 * @param collectionDataType the collectionDataType to set
	 */
	public void setCollectionDataType(String collectionDataType) {
		this.collectionDataType = collectionDataType;
	}

	/**
	 * @return the decimalPlaces
	 */
	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	/**
	 * @param decimalPlaces the decimalPlaces to set
	 */
	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * @return the xmlTagName
	 */
	public String getXmlTagName() {
		return xmlTagName;
	}

	/**
	 * @param xmlTagName the xmlTagName to set
	 */
	public void setXmlTagName(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}

	/**
	 * @return the isReverseValueUsed
	 */
	public boolean isReverseValueUsed() {
		return isReverseValueUsed;
	}

	/**
	 * @param isReverseValueUsed the isReverseValueUsed to set
	 */
	public void setReverseValueUsed(boolean isReverseValueUsed) {
		this.isReverseValueUsed = isReverseValueUsed;
	}

	/**
	 * @return the sampleValue
	 */
	public String getSampleValue() {
		return sampleValue;
	}

	/**
	 * @param sampleValue the sampleValue to set
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	/**
	 * @return the minValue
	 */
	public Long getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Long minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public Long getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the isXMLAttribute
	 */
	public boolean isXMLAttribute() {
		return isXMLAttribute;
	}

	/**
	 * @param isXMLAttribute the isXMLAttribute to set
	 */
	public void setXMLAttribute(boolean isXMLAttribute) {
		this.isXMLAttribute = isXMLAttribute;
	}

	/**
	 * @return the valueBasedResolvers
	 */
	public List<AttributeValueResolverModel> getValueBasedResolvers() {
		return valueBasedResolvers;
	}

	/**
	 * @param valueBasedResolvers the valueBasedResolvers to set
	 */
	public void setValueBasedResolvers(
			List<AttributeValueResolverModel> valueBasedResolvers) {
		this.valueBasedResolvers = valueBasedResolvers;
	}

	/**
	 * @return the validationConFigModels
	 */
	public List<AttributeValidationConfigModel> getValidationConFigModels() {
		return validationConFigModels;
	}

	/**
	 * @param validationConFigModels the validationConFigModels to set
	 */
	public void setValidationConFigModels(
			List<AttributeValidationConfigModel> validationConFigModels) {
		this.validationConFigModels = validationConFigModels;
	}
	
}
