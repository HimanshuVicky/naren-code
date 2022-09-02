package com.assignsecurities.domain.dm;

import java.io.Serializable;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;


//@Entity
//@Table(name = "dm_obj_attr_validation_dtl")
public class AttributeValidationConfigModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2773740680552507647L;
//	@Id
//	@Column(name = "ID", nullable = false)
//	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private long id;
	
//	@Column(name = "ATTRIBUTE_ID", nullable = false)
	private long attributeId;
	
//	@Column(name = "VALIDATTOR_CLASS_NAME", nullable = false)
	private String validatorClassName;
	
//	@Column(name = "VALIDATTOR_METHOD_NAME", nullable = false)
	private String validatorMethodName;
	
//	@Column(name = "VALIDATTOR_SEQ_NUMBER", nullable = false)
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
		
	public int compareTo(AttributeValidationConfigModel arg0) {		
		return this.sequenceNo.compareTo(arg0.getSequenceNo());
	}
}
