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
public class ReferralsCommisionDtlBean implements java.io.Serializable  {
		
	private static final long serialVersionUID = -6629922399009292810L;
	private Long id;
	private Long userId;
	private Long franchiseId;
	private Double registrationFee;
	private Double procesingFeeCommison;
	private Double agreementfeeCommision;
	private Double docuProcessingfeeCommision;
	private Boolean isRegistrationFeeReceived;
	private Integer eSignAgreementStatus;
	private String feeReferenceKey;
	
	private Long documentId;

}
