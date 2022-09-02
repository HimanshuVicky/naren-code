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
public class CaseWarrantDetailsBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642259667965420187L;
	private Long id;
	private Long caseScriptId;
	private String warrantNo;
	private String year;
	private Double amount;
	
}
