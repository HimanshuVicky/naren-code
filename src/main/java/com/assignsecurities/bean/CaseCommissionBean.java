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
public class CaseCommissionBean  implements java.io.Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 3036336508697475698L;
	private Long id;
	private Long caseId;
	private Long referralUserId;
	private Double referralProcFeeComm;
	private Double referralAgreFeeComm;
	private Double referralDocumentProcFeeComm;
	
	private String referralName;
}
