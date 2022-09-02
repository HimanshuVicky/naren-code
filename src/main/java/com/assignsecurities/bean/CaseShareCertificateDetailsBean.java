package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseShareCertificateDetailsBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7133596765032230181L;
	private Long id;
	private Long caseScriptId;
	private String certificateNo;
	private String distinctiveNo;
	private Double quantity;
	
}
