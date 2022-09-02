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
public class UpdateApplicationFeeBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2702714740086684670L;
	private Long applicationId;
	private String feeType;
	private Double feeValue;
	private String remarks;
}
