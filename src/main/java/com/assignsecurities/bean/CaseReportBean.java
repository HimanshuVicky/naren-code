package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CaseReportBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1032231370763069039L;
	private Long caseId;
	private String clientName;
	private String scriptName;
	private Double value;
	private String feeType;
	private Double agreementValue;
}
