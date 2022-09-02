package com.assignsecurities.bean;

import java.util.List;

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
public class CaseScriptUpdateBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2508937147755145723L;
	private Long caseId;
	private List<Long> removedCaseScriptIds;
	private List<AddExistingCaseScriptBean> addedScripts;
	private List<AddNewCaseScriptBean> newlyAddedScripts;
	
}
