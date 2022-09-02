package com.assignsecurities.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseQuestionerModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7282462614306052107L;
	private Long caseScriptId;
	private List<QuestionerModel> questioners;
	
	 
}
