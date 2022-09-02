package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchReferralModel {
	
	private Long id;
	private String clientName;
	private String contactNumber;
	private String state;
	private String city;
	private String referralPartner;
	private String franchiseName;
	private String registeredDate;
	private Double processingFee;
	private Double aggrementFee;
	private Double documentationFee;
	
}
