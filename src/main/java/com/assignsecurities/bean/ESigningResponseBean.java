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
public class ESigningResponseBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1180902743848787491L;
	private Long caseId;
	private Long referralUserId;
	private String clientId;
	private String token;
	private String url;
	
	private Boolean eSigningRequired;

}
