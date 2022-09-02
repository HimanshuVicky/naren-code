package com.assignsecurities.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
	private Long userId;
	private String stage;
	private String status;
	private String city;
	
	private Long franchiseId;
	
	private Long lawyerUserId;
	
	private String companyName;
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long referralUserId;
	
}
