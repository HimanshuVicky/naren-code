package com.assignsecurities.domain;

import java.util.Date;
import java.util.List;

public class AuditEvent {

	private Long id;
	
	private Date dateCreated;
	
	private String eventType;
	
	private String createdBy;

	private String desc;

	private List<AuditEventData> auditEventDatas;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the auditEventDatas
	 */
	public List<AuditEventData> getAuditEventDatas() {
		return auditEventDatas;
	}

	/**
	 * @param auditEventDatas the auditEventDatas to set
	 */
	public void setAuditEventDatas(List<AuditEventData> auditEventDatas) {
		this.auditEventDatas = auditEventDatas;
	}
	
	
}
