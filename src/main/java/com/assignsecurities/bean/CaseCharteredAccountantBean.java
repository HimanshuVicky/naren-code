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
public class CaseCharteredAccountantBean implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7375341216794268178L;
	private Long caseId;
	private Long charteredAccountantId;
	
	private UserLoginBean charteredAccountant;

}
