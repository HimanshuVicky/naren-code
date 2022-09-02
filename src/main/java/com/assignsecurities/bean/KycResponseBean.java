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
public class KycResponseBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3811034708211102787L;
	private String clientId;
	private Boolean otpSent;
	
	private Boolean otpRequired;

}
