package com.assignsecurities.domain.dm;

import java.io.Serializable;

public class AttributeValidationConfigBean implements Serializable, Comparable<AttributeValidationConfigBean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1504717288649079083L;
	private long id;
	private long attributeId;
	private String validatorClassName;
	private String validatorMethodName;
	private Long sequenceNo;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}
	public String getValidatorClassName() {
		return validatorClassName;
	}
	public void setValidatorClassName(String validatorClassName) {
		this.validatorClassName = validatorClassName;
	}
	public String getValidatorMethodName() {
		return validatorMethodName;
	}
	public void setValidatorMethodName(String validatorMethodName) {
		this.validatorMethodName = validatorMethodName;
	}
	public Long getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Long sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
		
	public int compareTo(AttributeValidationConfigBean arg0) {		
		return this.sequenceNo.compareTo(arg0.getSequenceNo());
	}

}
