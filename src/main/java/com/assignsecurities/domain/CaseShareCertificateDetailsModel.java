package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseShareCertificateDetailsModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 815754044187022090L;
	private Long id;
	private Long caseScriptId;
	private String certificateNo;
	private String distinctiveNo;
	private Double quantity;
	
	private Long scriptId;
	
	 
}
