
package com.assignsecurities.domain.dm;

import java.io.Serializable;


public class ObjectTemplateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4574356645526259652L;
	private long id;
	private long objId;
	private String localeCode;
	private byte[] template;
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
	 * @return the objId
	 */
	public long getObjId() {
		return objId;
	}
	/**
	 * @param objId the objId to set
	 */
	public void setObjId(long objId) {
		this.objId = objId;
	}
	/**
	 * @return the localeCode
	 */
	public String getLocaleCode() {
		return localeCode;
	}
	/**
	 * @param localeCode the localeCode to set
	 */
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	/**
	 * @return the template
	 */
	public byte[] getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(byte[] template) {
		this.template = template;
	}

	
}
