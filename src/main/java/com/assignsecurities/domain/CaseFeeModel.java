package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseFeeModel implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5819042257468693598L;
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
