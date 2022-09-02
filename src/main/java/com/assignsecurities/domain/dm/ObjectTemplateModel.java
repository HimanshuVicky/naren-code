package com.assignsecurities.domain.dm;
import java.io.Serializable;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//import javax.persistence.PrimaryKeyJoinColumn;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.Type;


//@Entity
//@Table(name = "dm_obj_template")
public class ObjectTemplateModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5627468668319295312L;
	
	private Long id;
	
	private Long objId;
	
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
