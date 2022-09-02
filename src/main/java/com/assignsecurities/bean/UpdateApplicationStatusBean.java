package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateApplicationStatusBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5567283178119429909L;
	private Long applicationId;
	private String status;
	private String remarks;
	
	private ApplicationFeeBean applicationFeeBean;
}
