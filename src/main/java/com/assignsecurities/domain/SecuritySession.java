package com.assignsecurities.domain;
// Generated 20 Jun, 2020 7:07:07 PM by Hibernate Tools 4.3.1.Final

import lombok.Data;

import java.util.Date;

@Data
public class SecuritySession implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4249456065663275805L;
	private Long id;
	private Long userId;
	private Date dateCreated;
	private String sessionId;
	private int validityPeriod;
}
