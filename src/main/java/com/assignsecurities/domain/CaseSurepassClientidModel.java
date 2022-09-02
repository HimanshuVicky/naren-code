package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseSurepassClientidModel implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7260748595148159913L;
	private Long id;
	private Long caseId;
	private String clientId;
	private String  reqType;
	private String reqStatus;
	private String remarks;
	private LocalDateTime createdDate;

}
