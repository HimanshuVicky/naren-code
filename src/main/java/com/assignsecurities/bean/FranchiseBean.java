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
public class FranchiseBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9029793390411281569L;
	
	private Long id;
	private Long franchiseId;
	private String name; 
	private String ownerFirstName;
	private String ownerLastName;
	private String emailId;
	private String contactNumber;
	private Long addressId;
	private Boolean isActive;
	private AddressBean address;
	private ApplicationUserBean applicationUser;
	private ReferralsCommisionDtlBean referralsCommisionDtlBean;
	
//	public Double referralRegistrationFee;
//	public Double referralProcFeecommission;
//	public Double referralAgrementFeecommission;
//	public Double documentProcssingFeecommission;
//	private Boolean isRegistrationFeeReceived;
//	private int eSignAgreementStatus;
	
	public Long getFranchiseId() {
		return id;
	}
}
