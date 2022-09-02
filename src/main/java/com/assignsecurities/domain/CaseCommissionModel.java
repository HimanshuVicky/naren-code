package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseCommissionModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6289581168216223349L;
	private Long id;
	private Long caseId;
	private Long referralUserId;
	private Double referralProcFeeComm;
	private Double referralAgreFeeComm;
	private Double referralDocumentProcFeeComm;
	
	private String referralName;


}
