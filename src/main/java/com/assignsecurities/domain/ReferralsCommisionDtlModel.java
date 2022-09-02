package com.assignsecurities.domain;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReferralsCommisionDtlModel implements java.io.Serializable {
	
	
	private static final long serialVersionUID = -7824281287638823570L;
	private Long id;
	private Long UserId;
	private Long franchiseId;
	private Double registrationFee;
	private Double procesingFeeCommison;
	private Double agreementfeeCommision;
	private Double docuProcessingfeeCommision;
	private Boolean isRegistrationFeeReceived;
	private Integer eSignAgreementStatus;
	private String feeReferenceKey;
	

}
