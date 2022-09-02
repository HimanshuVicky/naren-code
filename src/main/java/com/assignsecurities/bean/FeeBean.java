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
public class FeeBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3614904642878790166L;
	private Long id;
	private String feeFor;
	private String feeType;
	public Double feeValue;
	public Boolean isGSTApplicable;
}
