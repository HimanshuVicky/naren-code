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
public class CaseFranchiseBean implements java.io.Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -8182931341672362153L;
	private Long caseId;
	private Long franchiseId;
	
	private FranchiseBean franchise;
}
