package com.assignsecurities.domain.dm;
import java.io.Serializable;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;

//@Entity
//@Table(name = "dm_obj_attr_value_resolver")
public class AttributeValueResolverModel implements Serializable {

	private static final long serialVersionUID = 6363280039099705265L;

	// ID NUMBER(38,0) No
	// ATTRIBUTE_ID NUMBER(38,0) Yes
	// RESOLVER_CLASS_NAME VARCHAR2(255 CHAR) Yes
	// CODE_TO_RESOLVE VARCHAR2(255 CHAR) Yes
	// RESOLVER_TYPE VARCHAR2(20 CHAR) Yes
//	@Id
//	@Column(name = "ID", nullable = false)
//	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private long id;

//	@Column(name = "ATTRIBUTE_ID", nullable = false)
	private long attributeId;

//	@Column(name = "RESOLVER_CLASS_NAME", nullable = false)
	private String resolverClassName;

//	@Column(name = "CODE_TO_RESOLVE", nullable = false)
	private String codeToResolve;

//	@Column(name = "RESOLVER_TYPE", nullable = false)
	private String resolveType;


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the attributeId
	 */
	public long getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId
	 *            the attributeId to set
	 */
	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the resolverClassName
	 */
	public String getResolverClassName() {
		return resolverClassName;
	}

	/**
	 * @param resolverClassName
	 *            the resolverClassName to set
	 */
	public void setResolverClassName(String resolverClassName) {
		this.resolverClassName = resolverClassName;
	}

	/**
	 * @return the codeToResolve
	 */
	public String getCodeToResolve() {
		return codeToResolve;
	}

	/**
	 * @param codeToResolve
	 *            the codeToResolve to set
	 */
	public void setCodeToResolve(String codeToResolve) {
		this.codeToResolve = codeToResolve;
	}

	/**
	 * @return the resolveType
	 */
	public String getResolveType() {
		return resolveType;
	}

	/**
	 * @param resolveType
	 *            the resolveType to set
	 */
	public void setResolveType(String resolveType) {
		this.resolveType = resolveType;
	}
}
