package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyActionReferralModel {
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
