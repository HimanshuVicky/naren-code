package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserModel {
	
	private Long id;
	private String clientName;
	private String contactNumber;
	private String applicationYes;
	private String caseYes;
	private String pin;
	private String franchiseName;
	private String city;
	private LocalDateTime loginDate;
	private String userType;
	private String referralName;
	
}
