
package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class AttributeConfigBean implements Serializable{
	private long id;
	private String name;
	private String dataType;
	private boolean isRequired;
	private int length;
	private long parentObjectId;
	private Long childObjectId;	
	private boolean isAttributeAvailableInBo;
	private Long sequenceNo;	
	private String dataFormatKey;
	private String delimitedKey;
	private String collectionDataType;
	private String headerName;
	private Integer decimalPlaces=-1;
	private List<AttributeValidationConfigBean> validationConFigModels;
	private Map<String,String> valueBasedResolver;
	private boolean isXMLAttribute;
	private String xmlTagName;
	private String sampleValue;
	private boolean isReverseValueUsed;
	private Long minValue;
	private Long maxValue;
	public String getXmlTagName() {
		return xmlTagName;
	}
	public void setXmlTagName(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		if(name != null && name.contains(".")){
			return name.substring(name.lastIndexOf(".")+1);
		}
		return name;
	}
	public String getFullyQualifiedName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullyQualifiedDataType() {
		return dataType;
	}

	public String getDataType() {
		if (dataType != null && dataType.contains("|")) {
			return dataType.substring(dataType.lastIndexOf("|") + 1);
		}
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getParentObjectId() {
		return parentObjectId;
	}
	public void setParentObjectId(long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}
	public Long getChildObjectId() {
		return childObjectId;
	}
	public void setChildObjectId(Long childObjectId) {
		this.childObjectId = childObjectId;
	}
	public boolean isAttributeAvailableInBo() {
		return isAttributeAvailableInBo;
	}
	public void setAttributeAvailableInBo(boolean isAttributeAvailableInBo) {
		this.isAttributeAvailableInBo = isAttributeAvailableInBo;
	}
	public Long getSequenceNo() {
		return sequenceNo;
	}
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

	public int compareTo(AttributeConfigBean o) {
		return this.getSequenceNo().compareTo(o.getSequenceNo());
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
	public String getCollectionDataType() {
		return collectionDataType;
	}
	public void setCollectionDataType(String collectionDataType) {
		this.collectionDataType = collectionDataType;
	}
	public List<AttributeValidationConfigBean> getValidationConFigModels() {
		return validationConFigModels;
	}
	public void setValidationConFigModels(List<AttributeValidationConfigBean> validationConFigModels) {
		this.validationConFigModels = validationConFigModels;
	}
	public String getHeaderName() {
		return headerName;
	}
	
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
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
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String newLine = System.getProperty("line.separator");

	    result.append( this.getClass().getName() );
	    result.append( " Object {" );
	    result.append(newLine);

	    //determine fields declared in this class only (no fields of superclass)
	    Field[] fields = this.getClass().getDeclaredFields();

	    //print field names paired with their values
	    for ( Field field : fields  ) {
	      result.append("  ");
	      try {
	        result.append( field.getName() );
	        result.append(": ");
	        //requires access to private field:
	        result.append( field.get(this) );
	      }
	      catch ( IllegalAccessException ex ) {
	        System.out.println(ex);//TODO
	      }
	      result.append(newLine);
	    }
	    result.append("}");

	    return result.toString();

	}
	public Map<String,String> getValueBasedResolver() {
		return valueBasedResolver;
	}
	public void setValueBasedResolver(Map<String,String> valueBasedResolver) {
		this.valueBasedResolver = valueBasedResolver;
	}
	public void setXMLAttribute(boolean isXMLAttribute) {
		this.isXMLAttribute = isXMLAttribute;
	}
	public boolean isXMLAttribute() {
		return isXMLAttribute;
	}
	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}
	public String getSampleValue() {
		return sampleValue;
	}
	public void setReverseValueUsed(boolean isReverseValueUsed) {
		this.isReverseValueUsed = isReverseValueUsed;
	}
	public boolean isReverseValueUsed() {
		return isReverseValueUsed;
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
	
}
