package com.assignsecurities.domain.dm;

import java.io.Serializable;

public class AttributeValueResolverBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 209797932145001111L;
	private long id;

	private long attributeId;

	private String resolverClassName;

	private String codeToResolve;

	private String resolveType;
}
