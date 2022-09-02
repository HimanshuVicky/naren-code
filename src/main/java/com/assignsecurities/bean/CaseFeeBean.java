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
public class CaseFeeBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4996217949512829108L;
	private Long id;
	private Long caseId;
	private String templateType;
	private String feeFor;
	private String feeType;
	private Double feeValue;
	private String refNo;
	private Boolean isGSTApplicable;
	private Double receivedFeeValue;
}
