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
public class SearchReferralBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8734229120712040413L;
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
