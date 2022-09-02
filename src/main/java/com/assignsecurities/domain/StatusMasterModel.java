package com.assignsecurities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusMasterModel {
	private Long id;
	private String stage;
	private String status;
	private String customer;
	private String customerCare;
	private String admin;
	private String franchiseOwner;
	private String franchiseUser;
	private String lawyer;
	private Boolean autoUpdate;
	private String flag;
	private Boolean isActive;
	private Integer tempOrder;
}
