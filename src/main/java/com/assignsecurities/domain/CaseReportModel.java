package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseReportModel {
	private Long caseId;
	private String clientName;
	private String scriptName;
	private Double value;
	private String feeType;
	private Double agreementValue;
}
