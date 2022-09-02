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
public class ReportBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4116565658403908766L;
	private Long userId;
	private String duration;
	private String city;

	private String stage;
	private String status;
	private Long franchiseId;
	private Long assignLawyerId;
//	private Long scriptId;
	private String companyName;
	private Long referralUserId;
}
