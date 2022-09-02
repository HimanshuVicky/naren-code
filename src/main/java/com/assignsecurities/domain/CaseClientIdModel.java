package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseClientIdModel implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6292105739167051222L;
	private Long id;
	private Long caseId;
	private String clientId1;
	private String clientId2;

}
