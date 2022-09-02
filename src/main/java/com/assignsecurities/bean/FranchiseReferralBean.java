package com.assignsecurities.bean;

import com.assignsecurities.bean.FranchiseBean.FranchiseBeanBuilder;
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
public class FranchiseReferralBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5535090877894943013L;
	
	private Long franchiseId;
	
	private String referralUrl;
}
