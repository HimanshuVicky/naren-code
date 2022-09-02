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
public class CaseStatusUpdateBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3647730612328413797L;

	private Long caseId;
	
	private String status;
	
}
