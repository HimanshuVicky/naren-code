package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1848766727070653463L;

	private Long id; 
	private String name; 
	private String ownerFirstName;
	private String ownerLastName;
	private String emailId;
	private String contactNumber;
	private Long addressId;
	private Boolean isActive;
//	public Double referralRegistrationFee;
//	public Double referralProcFeecommission;
//	public Double referralAgrementFeecommission;
//	public Double documentProcssingFeecommission;
//	private Boolean isRegistrationFeeReceived;
//	private int eSignAgreementStatus;
	
	private AddressModel address;
	//TODO franchise

}
