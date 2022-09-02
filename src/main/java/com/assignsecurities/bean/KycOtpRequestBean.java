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
public class KycOtpRequestBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9191282182093471056L;
	private Long caseId;
	private String clientId;
	private String otp;

}
