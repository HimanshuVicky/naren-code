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
public class MyActionReferralBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098656895769739355L;
	private String clientName;
	private String contactNumber;
	private Long franchiseId;
	private Long userId;
	private Long referralUserId;
	private String feePaid;
	private Double feeAmount;
	private String status;
	private String userType;
	private String feeReferenceKey;
}
